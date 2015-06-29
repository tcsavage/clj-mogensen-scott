(defproject clj-scott-adts "0.1.0-SNAPSHOT"
  :description "ADTs in Clojure using Scott encoding"
  :url "https://github.com/tcsavage/clj-scott-adts"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [camel-snake-kebab "0.3.1" :exclusions [org.clojure/clojure]]
                 [potemkin "0.3.13"]]
  :plugins [[codox "0.8.10"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]
                   :source-paths ["dev"]}})
