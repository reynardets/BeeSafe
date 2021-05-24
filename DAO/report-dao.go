package DAO

import (
	"beesafe-api/model"
	"cloud.google.com/go/firestore"
	"context"
	"google.golang.org/api/iterator"
	"google.golang.org/api/option"
	"log"
	"strconv"
	"strings"
)

type ReportDao interface {
	AddReport(report *model.Report) (*model.Report, error)
	//UpdateReport(report *model.Report) (*model.Report, error)
	GetReports() ([]model.Report, error)
	GetReportsByLocation(latitude float64, longitude float64, distance float64) ([]model.Report, error)
}

type dao struct{}

func NewReportDao() ReportDao {
	return &dao{}
}

const (
	projectId      string = "mydemo-d675a"
	collectionName string = "reports"
)

func (*dao) AddReport(report *model.Report) (*model.Report, error) {
	ctx := context.Background()
	client, err := firestore.NewClient(ctx, projectId, option.WithCredentialsFile("./service-account.json"))
	if err != nil {
		log.Fatalf("Failed to create a Firestore Client: %v", err)
		return nil, err
	}
	defer client.Close()

	_, _, err = client.Collection(collectionName).Add(ctx, map[string]interface{}{
		"category":    report.Category,
		"datetime":    report.Datetime,
		"description": report.Description,
		"latitude":    report.Latitude,
		"longitude":   report.Longitude,
		"userId":      report.UserId,
	})

	if err != nil {
		log.Fatalf("Failed adding a new report: %v", err)
		return nil, err
	}

	return report, nil
}

func (*dao) GetReports() ([]model.Report, error) {
	ctx := context.Background()
	client, err := firestore.NewClient(ctx, projectId, option.WithCredentialsFile("./service-account.json"))
	if err != nil {
		log.Fatalf("Failed to create a Firestore Client: %v", err)
		return nil, err
	}

	defer client.Close()
	var reports []model.Report
	it := client.Collection(collectionName).Documents(ctx)
	for {
		doc, err := it.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			log.Fatalf("Failed to iterate the list of reports: %v", err)
			return nil, err
		}
		report := model.Report{
			Category:    doc.Data()["category"].(string),
			Datetime:    doc.Data()["datetime"].(string),
			Description: doc.Data()["description"].(string),
			Longitude:   doc.Data()["longitude"].(string),
			Latitude:    doc.Data()["latitude"].(string),
			UserId:      doc.Data()["userId"].(string),
		}
		reports = append(reports, report)
	}
	return reports, nil
}

func (*dao) GetReportsByLocation(latitude float64, longitude float64, distance float64) ([]model.Report, error) {

	// ~1 mile of lat and lon in degrees
	lat := float64(0.0144927536231884)
	lon := float64(0.0181818181818182)

	lowerLat := float64(latitude - (lat * distance))
	lowerLon := float64(longitude - (lon * distance))

	greaterLat := float64(latitude + (lat * distance))
	greaterLon := float64(longitude + (lon * distance))

	println("Origin :", latitude, longitude, "\nLat : ", lowerLat, greaterLat, "\nLong", lowerLon, greaterLon)

	ctx := context.Background()
	client, err := firestore.NewClient(ctx, projectId, option.WithCredentialsFile("./service-account.json"))
	if err != nil {
		log.Fatalf("Failed to create a Firestore Client: %v", err)
		return nil, err
	}

	defer client.Close()
	var reports []model.Report
	it := client.Collection(collectionName).Documents(ctx)
	for {
		doc, err := it.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			log.Fatalf("Failed to iterate the list of reports: %v", err)
			return nil, err
		}

		dbLongitude, _ := strconv.ParseFloat(strings.TrimSpace(doc.Data()["longitude"].(string)), 64)
		dbLatitude, _ := strconv.ParseFloat(strings.TrimSpace(doc.Data()["latitude"].(string)), 64)

		if (dbLatitude >= lowerLat && dbLongitude >= lowerLon) && (dbLongitude <= greaterLon && dbLatitude <= greaterLat) {
			report := model.Report{
				Category:    doc.Data()["category"].(string),
				Datetime:    doc.Data()["datetime"].(string),
				Description: doc.Data()["description"].(string),
				Longitude:   doc.Data()["longitude"].(string),
				Latitude:    doc.Data()["latitude"].(string),
				UserId:      doc.Data()["userId"].(string),
			}
			reports = append(reports, report)
		}

	}
	return reports, nil
}
