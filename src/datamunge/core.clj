(ns datamunge.core
  (:use [midje.sweet :only (unfinished)])
  (:require [clojure-csv.core :as csv]))

(unfinished convert-date most-recent-meeting-with)

(defn parse-csv-input-string [input-string]
  (map #(update-in % [:date] convert-date)
       (map #(zipmap [:date :names :organisation :cause] %) (csv/parse-csv input-string))))

(defn parse-data-file [filename]
  (parse-csv-input-string (slurp filename)))

(defn most-recent-meeting-with-a-murdoch [filename]
  (let [data-map (parse-data-file filename)]
    (most-recent-meeting-with data-map "Murdoch")))
    
