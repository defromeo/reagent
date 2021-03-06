(ns geometry.core
  (:require 
   [reagent.core :as r]
   [geometry.components :as c]
   [geometry.geometry :as g]))

(enable-console-print!)

;; "Global" mouse events
(def mouse-info 
  (r/atom {:x 0
           :y 0
           :mouse-down? false}))

(defn on-mouse-move [evt node]
  (let [bcr (-> node .getBoundingClientRect)]
    (swap! mouse-info assoc
           :x (- (.-clientX evt) (.-left bcr))
           :y (- (.-clientY evt) (.-top bcr)))))

(defn on-mouse-up [evt]
  (swap! mouse-info assoc :mouse-down? false))

(defn on-mouse-down [evt]
  (swap! mouse-info assoc :mouse-down? true))

(def p1 (r/atom (g/point 100 100)))
(def p2 (r/atom (g/point 200 200)))
(def p3 (r/atom (g/point 100 200)))
(def c (r/atom (g/point 250 250)))
(def p (r/atom (g/point 250 300)))

(defn root []
  [:g
   [c/triangle @p1 @p2 @p3]
   [c/circle @p @c]
   [c/segment @p @c]
   [c/draggable-point c mouse-info]
   [c/draggable-point p mouse-info]
   [c/draggable-point p1 mouse-info]
   [c/draggable-point p2 mouse-info]
   [c/draggable-point p3 mouse-info]])

(defn main [{:keys [width height]}]
  (let [this (r/current-component)]
    [:svg {:on-mouse-down on-mouse-down
           :on-mouse-up on-mouse-up
           :on-mouse-move #(on-mouse-move % (r/dom-node this))
           :width (or width 800)
           :height (or height 600)
           :style {:border "1px solid black"}}
     [:text {:style {:-webkit-user-select "none"
                     :-moz-user-select "none"}
             :x 20 :y 20 :font-size 20}
      "The points are draggable"]
     [root]]))

(defn by-id [id]
  (.getElementById js/document id))

(defn ^:export run []
  (r/render-component 
   [main]
   (by-id "app")))
