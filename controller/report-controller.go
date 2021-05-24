package controller

import (
	"beesafe-api/DAO"
	"beesafe-api/model"
	"encoding/json"
	"net/http"
)

var (
	dao DAO.ReportDao = DAO.NewReportDao()
)

func GetReports(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	reports, err := dao.GetReports()
	if err != nil {
		response.WriteHeader(http.StatusInternalServerError)
		response.Write([]byte(`{"error": "Error getting the reports"}`))
	}
	response.WriteHeader(http.StatusOK)
	json.NewEncoder(response).Encode(reports)
}

func AddReport(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	var report model.Report
	err := json.NewDecoder(request.Body).Decode(&report)
	if err != nil {
		response.WriteHeader(http.StatusInternalServerError)
		response.Write([]byte(`{"error": "Error unmarshalling data"}`))
		return
	}
	dao.AddReport(&report)
	response.WriteHeader(http.StatusOK)
	json.NewEncoder(response).Encode(report)
}
