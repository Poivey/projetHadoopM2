(() => {
    google.charts.load("current", {packages:["corechart"]});

    const drawBasic = (rows) => {

        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Time window');
        data.addColumn('number', 'Instances');

        data.addRows(rows);

        const options = {
            title: 'Instances of crime per time window',
            pieHole: 0.4,
            height: 500
        };

        const chart = new google.visualization.PieChart(document.getElementById('chart_div'));

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
        fetch('../js/output/crime2/part-r-00000')
            .then(response => response.text())
            .then(text => {
                drawBasic(makeTable(text.split('\n')))
            })
    }

    google.charts.setOnLoadCallback(getAndDrawData);
})();

