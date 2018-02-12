(() => {
    google.charts.load('current', {packages: ['corechart', 'bar']});

    const drawChart = (rows) => {

        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Crime');
        data.addColumn('number', 'Instances');

        data.addRows(rows);

        const options = {
            legend: {position: 'none'},
            title: 'Instances of crime per type',
            vAxis: {
                title: 'Instances'
            },
            hAxis: {
                slantedTextAngle: 45
            },
            height: 500
        };

        const chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));

        chart.draw(data, options);
    }
    const makeTable = lines => {
        table = []
        lines.forEach(line => {
            if(line !== ""){
                const tableLine = line.split('\t')
                table.push([tableLine[0], parseInt(tableLine[1])])
            }
        })
        return table
    }
    const getAndDrawData = () => {
        fetch('../js/output/crime1/part-r-00000')
            .then(response => response.text())
            .then(text => {
                drawChart(makeTable(text.split('\n')))
            })
    }

    google.charts.setOnLoadCallback(getAndDrawData);
})();

