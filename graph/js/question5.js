(() => {
    google.charts.load('current', {packages: ['corechart', 'bar']});

    const drawChart = (rows) => {

        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Month');
        data.addColumn('number', 'Instances');

        data.addRows(rows);

        const options = {
            legend: {position: 'none'},
            title: 'Top 3 Criminal months',
            vAxis: {
                title: 'Instances'
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
        return table.reverse()
    }
    const getAndDrawData = () => {
        fetch('../js/output/crime5/part-r-00000')
            .then(response => response.text())
            .then(text => {
                drawChart(makeTable(text.split('\n')))
            })
    }

    google.charts.setOnLoadCallback(getAndDrawData);
})();

