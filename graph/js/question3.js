(() => {
  var mymap = L.map('mapLeaflet').setView([41.880042, -87.682915], 11)
  L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoicG9pdjgiLCJhIjoiY2pld3BkdHdxMHRxdjMzcHV4bTV5dHY0cCJ9.EiKOcaCwcr_9Rscl5GZy6w', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets'
  }).addTo(mymap)


  const drawCircles = (localisationArray) => {
    let i = 0
    localisationArray.forEach(line => {
      if(line !== ""){
        let [localisation, crimeCount] = line.split('\t')
        L.circle([localisation.split(" ")[0], localisation.split(" ")[1]], {
          color: (i < 3) ? 'red' : 'green',
          fillColor: (i < 3) ? 'red' : 'green',
          fillOpacity: 0.3,
          radius: 2000
        }).bindPopup(`Nombre de crime : ${crimeCount}`).addTo(mymap)
      }
      i += 1
    })
  }

  const getAndDrawData = () => {
    fetch('../js/output/crime3-top/part-r-00000')
      .then(response => response.text())
      .then(text => drawCircles(text.split('\n')))
  }
  getAndDrawData()
})();

