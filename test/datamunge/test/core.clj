(ns datamunge.test.core
  (:use [datamunge.core])
  (:use clojure.test midje.sweet))

(let [input-string "\"Apr, 10\",\"Rupert Murdoch \",\"News Corp\",\"General discussion\""]
  (against-background [(convert-date "Apr, 10") => "2010-04"]
    (fact (first (parse-csv-input-string input-string)) =>
      (contains {:names "Rupert Murdoch " :organisation "News Corp" :cause "General discussion"}))
    (fact (first (parse-csv-input-string input-string)) => (contains {:date "2010-04"}))))
      

(fact (parse-data-file ...file-name...) => ...parsed-file-map...
  (provided (slurp ...file-name...) => ...input-string...
            (parse-csv-input-string ...input-string...) => ...parsed-file-map...))

(fact (most-recent-meeting-with-a-murdoch "david-cameron-meetings.csv") => "2010-10"
  (provided (parse-data-file "david-cameron-meetings.csv") => ...parsed-file-map...
            (most-recent-meeting-with ...parsed-file-map... "Murdoch") => "2010-10"))
