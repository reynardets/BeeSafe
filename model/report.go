package model

type Report struct {
	Category    string `json:"category"`
	Datetime    string `json:"datetime"`
	Description string `json:"description"`
	UserId      string `json:"userId"`
	Latitude    string `json:"latitude"`
	Longitude   string `json:"longitude"`
}
