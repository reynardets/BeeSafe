package main

import (
	"beesafe-api/controller"
	"fmt"
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

func main() {
	router := mux.NewRouter()

	const port string = ":8080"

	router.HandleFunc("/", func(writer http.ResponseWriter, request *http.Request) {
		fmt.Fprint(writer, "Hello there , im online !")
	})
	router.HandleFunc("/reports", controller.GetReports).Methods("GET")
	router.HandleFunc("/reports/{location}", controller.GetReports).Methods("GET") //get data by location
	router.HandleFunc("/report", controller.AddReport).Methods("POST")
	log.Println("Server listening on port", port)
	log.Fatalln(http.ListenAndServe(port, router))
}
