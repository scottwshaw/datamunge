(ns datamunge.core
  (:use [midje.sweet :only (unfinished)])
  (:require [clojure.contrib.string :as ccs]
            [clojure-csv.core :as csv]))

(defn convert-date [month-year-string]
  (let [month-number-map {"Jan" "01" "Feb" "02" "Mar" "03" "Apr" "04" "May" "05" "Jun" "06"
                          "Jul" "07" "Aug" "08" "Sep" "09" "Oct" "10" "Nov" "11" "Dec" "12"}
        month (ccs/take 3 month-year-string)
        year (ccs/tail 2 month-year-string)]
    (str "20" year "-" (get month-number-map month "00"))))

(defn parse-csv-input-string [input-string]
  (map #(update-in % [:date] convert-date)
       (map #(zipmap [:date :names :organisation :cause] %) (csv/parse-csv input-string))))

(defn parse-data-file [filename]
  (parse-csv-input-string (slurp filename)))

(defn name-contains? [name input-map]
  (re-find (re-pattern (str "\\w+\\s" name)) (:names input-map)))

(defn most-recent-meeting-with [name parsed-data-file]
  (last (filter #(name-contains? name %) (sort-by :date parsed-data-file))))

(defn most-recent-meeting-with-a-murdoch [filename]
  (let [data-map (parse-data-file filename)]
    (most-recent-meeting-with "Murdoch" data-map)))


