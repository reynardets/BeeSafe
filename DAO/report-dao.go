package DAO

import (
	"beesafe-api/model"
	"cloud.google.com/go/firestore"
	"context"
	"google.golang.org/api/iterator"
	"google.golang.org/api/option"
	"log"
)

type ReportDao interface {
	AddReport(report *model.Report) (*model.Report, error)
	//UpdateReport(report *model.Report) (*model.Report, error)
	GetReports() ([]model.Report, error)
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
