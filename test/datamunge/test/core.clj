(ns datamunge.test.core
  (:use [datamunge.core])
  (:use clojure.test midje.sweet))

(tabular 
 (fact
   (convert-date ?word-date) => ?number-date)
 :where
 | ?word-date | ?number-date
 | "Jan, 11"  | "2011-01"
 | "Apr, 10"  | "2010-04"
 | "Oct, 10"  | "2010-10")

(def input-string
  (str "\"Apr, 10\",\"Rupert Murdoch \",\"News Corp\",\"General discussion\"
\"Oct, 10\",\"James Murdoch \",\"News Corp\",\"Chequers\"
\"Jan, 11\",\"Regional Lobby\",\"\",\"Reception (No 10)\""))

(against-background [(convert-date "Apr, 10") => "2010-04"
                     (convert-date "Oct, 10") => "2010-10"]
  (fact (first (parse-csv-input-string input-string)) =>
    (just {:date "2010-04" :names "Rupert Murdoch " :organisation "News Corp" :cause "General discussion"}))
  (fact (second (parse-csv-input-string input-string)) =>
    (just {:date "2010-10" :names "James Murdoch " :organisation "News Corp" :cause "Chequers"})))

(fact (parse-data-file ...file-name...) => ...parsed-file-map...
  (provided (slurp ...file-name...) => ...input-string...
            (parse-csv-input-string ...input-string...) => ...parsed-file-map...))

(tabular 
 (fact (name-contains? ?name ?input-map) => ?result)
 :where
 | ?name   | ?input-map   | ?result
 | "roger" | {:names "sam"}        | falsey
 | "roger" | {:names "sam, roger"} | truthy)

(fact (most-recent-meeting-with-a-murdoch "david-cameron-meetings.csv") => "2010-10"
  (provided (parse-data-file "david-cameron-meetings.csv") => ...parsed-file-map...
            (most-recent-meeting-with "Murdoch" ...parsed-file-map...) => "2010-10"))

(let [parsed-file-map [{:date "2010-04" :names "Rupert Murdoch " :organisation "News Corp" :cause "General discussion"}
                       {:date "2010-10" :names "James Murdoch " :organisation "News Corp" :cause "Chequers"}
                       {:date "2010-06" :names "Tony Gallagher " :organisation "Telegraph" :cause"Social "}]]
  (fact (most-recent-meeting-with "Murdoch" parsed-file-map) => (contains {:date "2010-10" :names "James Murdoch "})))

(future-fact (most-recent-meeting-with-a-murdoch "data/meetings.csv") => (contains {:date "2010-10"}))

