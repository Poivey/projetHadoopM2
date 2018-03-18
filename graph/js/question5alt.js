(() => {
  google.charts.load('current', {packages: ['corechart', 'bar']})

  const drawAllChart = (map) => {
    map.forEach((values, key) => drawChart(key, values))
  }

  const drawChart = (key, values) => {
    const month = {
      '01': 'january',
      '02': 'february',
      '03': 'march',
      '04': 'april',
      '05': 'may',
      '06': 'june',
      '07': 'july',
      '08': 'august',
      '09': 'september',
      '10': 'october',
      '11': 'november',
      '12': 'december',
    }

    const data = new google.visualization.DataTable()
    data.addColumn('string', 'Month')
    data.addColumn('number', 'Instances')

    chartValues = []
    values.forEach(monthAndNumber => {
      chartValues.push([month[monthAndNumber[0]], parseInt(monthAndNumber[1], 10)])
    })

    data.addRows(chartValues)

    const options = {
      legend: {position: 'none'},
      title: `Top 3 Criminal months for ${key}`,
      vAxis: {
        minValue: 0,
        title: 'Instances'
      },
      height: 500
    }

    const divChart = document.createElement('div')
    divChart.id = `${key}Chart`
    divChart.classList.add('graph')
    const chart = new google.visualization.ColumnChart(divChart)

    chart.draw(data, options)

    document.getElementById('container-graph').appendChild(divChart)
  }
  const makeMap = lines => {
    const map = new Map()
    lines.forEach(line => {
      if (line !== '') {
        const [crime, value] = line.split(':')
        if (map.has(crime)){
          map.get(crime).push(value.split('\t'))
        } else {
          map.set(crime, [value.split('\t')])
        }
      }
    })
    return map
  }
  const getAndDrawData = () => {
    fetch('../js/output/crime5alt/part-r-00000')
      .then(response => response.text())
      .then(text => {
        drawAllChart(makeMap(text.split('\n')))
        // console.log(makeMap(text.split('\n')))
      })
  }

  google.charts.setOnLoadCallback(getAndDrawData)
})()

