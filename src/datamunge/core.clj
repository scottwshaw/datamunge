(ns datamunge.core
  (:use [midje.sweet :only (unfinished)])
  (:require [clojure.contrib.string :as ccs]
            [clojure-csv.core :as csv]))

(unfinished most-recent-meeting-with)


(defn convert-date [month-year-string]
  (let [month-number-map {"Apr" "04" "Oct" "10"}
        month (ccs/take 3 month-year-string)
        year (ccs/tail 2 month-year-string)]
    (str "20" year "-" (get month-number-map month))))
        

(defn parse-csv-input-string [input-string]
  (map #(update-in % [:date] convert-date)
       (map #(zipmap [:date :names :organisation :cause] %) (csv/parse-csv input-string))))

(defn parse-data-file [filename]
  (parse-csv-input-string (slurp filename)))

(defn most-recent-meeting-with-a-murdoch [filename]
  (let [data-map (parse-data-file filename)]
    (most-recent-meeting-with data-map "Murdoch")))
    
