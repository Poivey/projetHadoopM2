(() => {
  var arrestedMap = L.map('mapLeafletArrested').setView([41.880042, -87.682915], 10)
  var notArrestedMap = L.map('mapLeafletNotArrested').setView([41.880042, -87.682915], 10)
  L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoicG9pdjgiLCJhIjoiY2pld3BkdHdxMHRxdjMzcHV4bTV5dHY0cCJ9.EiKOcaCwcr_9Rscl5GZy6w', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets'
  }).addTo(arrestedMap)
  L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoicG9pdjgiLCJhIjoiY2pld3BkdHdxMHRxdjMzcHV4bTV5dHY0cCJ9.EiKOcaCwcr_9Rscl5GZy6w', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets'
  }).addTo(notArrestedMap)


  const drawHeatMaps = (crimeArray) => {
    let i = 0

    let arrestedArray = []
    let maxArrested = 0

    let notArrestedArray = []
    let maxNotArrested = 0
    crimeArray.forEach(crime => {
      if(crime !== ""){
        let [info, crimeCount] = crime.split('\t')
        let [lat, long, resolved] = info.split(' ')
        if (resolved === 'true'){
          if (crimeCount > maxArrested){maxArrested = crimeCount}
          arrestedArray.push([lat, long, crimeCount])
        } else {
          if (crimeCount > maxNotArrested){maxNotArrested = crimeCount}
          notArrestedArray.push([lat, long, crimeCount])
        }
      }
    })
    L.heatLayer(arrestedArray, {
      max: maxArrested,
      radius: 30
    }).addTo(arrestedMap)

    L.heatLayer(notArrestedArray, {
      max: maxNotArrested,
      radius: 13
    }).addTo(notArrestedMap)
  }

  const getAndDrawData = () => {
    fetch('../js/output/crime4/part-r-00000')
      .then(response => response.text())
      .then(text => drawHeatMaps(text.split('\n')))
  }
  getAndDrawData()
})();

