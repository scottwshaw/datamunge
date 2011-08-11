(ns datamunge.test.core
  (:use [datamunge.core])
  (:use clojure.test midje.sweet))

(facts
  (convert-date "Apr, 10") => "2010-04"
  (convert-date "Oct, 10") => "2010-10")

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

(fact (most-recent-meeting-with-a-murdoch "david-cameron-meetings.csv") => "2010-10"
  (provided (parse-data-file "david-cameron-meetings.csv") => ...parsed-file-map...
            (most-recent-meeting-with ...parsed-file-map... "Murdoch") => "2010-10"))

(let [parsed-file-map [{:date "2010-04" :names "Rupert Murdoch " :organisation "News Corp" :cause "General discussion"}
                       {:date "2010-10" :names "James Murdoch " :organisation "News Corp" :cause "Chequers"}
                       {:date "2010-06" :names "Tony Gallagher " :organisation "Telegraph" :cause"Social "}]]
  (fact (most-recent-meeting-with parsed-file-map "Murdoch") => (contains {:date "2010-10" :names "James Murdoch "})))