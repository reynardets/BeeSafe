from typing import Optional
from pydantic import BaseModel
from fastapi import FastAPI
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from six import u
import requests
import json
from datetime import datetime, timedelta, timezone
from google.api_core.datetime_helpers import DatetimeWithNanoseconds


app = FastAPI()

# Use the credentials service account
cred = credentials.Certificate('credentials.json')
firebase_admin.initialize_app(cred)

#create client
db = firestore.client()


class Report(BaseModel):
    category : str
    datetime : str
    description : str
    latitude: float
    longitude: float
    userId: str


def getCategory(desc):
    url = 'http://34.101.97.194:8008/predict'
    body =  {"kalimat": desc}

    req = requests.post(url,data=json.dumps(body))

    return req.json()["data"]

#Http route
@app.get("/")
def read_root():
    return {"message": "Hello, Im online"}

@app.post("/report")
def createNewReport(request: Report):
    data = {
        "category":getCategory(request.description),
        "datetime":datetime.strptime(request.datetime, '%d/%m/%Y'),
        "description":request.description,
        "location":firestore.GeoPoint(request.latitude,request.longitude),
        "userId":request.userId
    }
    doc_ref = db.collection(u'reports').document()
    doc_ref.set(data)
    return {"message":"success adding data", "data":request}

@app.get("/reports")
def getReports():
    doc_ref = db.collection(u'reports')
    docs = doc_ref.stream()

    data = [];
    for doc in docs:
        data.append(doc.to_dict())
    return {"message":"success","data":data}

@app.get("/reports/location")
def getReportsByLocation(latitude:float, longitude:float):

    # ~1 mile of lat and lon in degrees
    lat = 0.0144927536231884
    long = 0.0181818181818182
    distance = 0.5 #0.5 mile or ~1km

    lowerLat = (latitude - (lat * distance))
    lowerLong = (longitude - (long * distance))

    greaterLat = (latitude + (lat * distance))
    greaterLong = (longitude + (long * distance))

    lesserGeoPoint = firestore.GeoPoint(lowerLat,lowerLong)
    greaterGeoPoint = firestore.GeoPoint(greaterLat,greaterLong)

    #make a datetime now and a week before
    today = datetime.now() + timedelta(days=1) #adding + 1 day , to compare with UTC timezone
    week_ago = today - timedelta(days=7)

    doc_ref = db.collection(u'reports').where(u'location', u'>=',lesserGeoPoint).where(u'location',u'<=',greaterGeoPoint)
    docs = doc_ref.stream()

    data = [];

    for doc in docs:
        #make DatetimeWithNanoseconds from datetime.datetime 
        today_DTM = DatetimeWithNanoseconds(today.year,today.month, today.day,0,0,tzinfo=timezone.utc)
        week_ago_DTM = DatetimeWithNanoseconds(week_ago.year,week_ago.month, week_ago.day,0,0,tzinfo=timezone.utc)

        if(doc.to_dict()["datetime"] >= week_ago_DTM and doc.to_dict()["datetime"] <= today_DTM):
            data.append(doc.to_dict())

    return {"message":"success","data":data}



