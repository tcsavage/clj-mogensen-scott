(defproject clj-mogensen-scott "0.1.0-SNAPSHOT"
  :description "ADTs in Clojure using Mogensen-Scott encoding"
  :url "https://github.com/tcsavage/clj-mogensen-scott"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [camel-snake-kebab "0.3.1" :exclusions [org.clojure/clojure]]
                 [potemkin "0.3.13"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]
                   :source-paths ["dev"]}})
