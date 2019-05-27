const express = require('express');
var app = express()
var bodyParser = require("body-parser");
const fs = require('fs');
var os = require("os");


app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.json());


app.use((req,res,next)=>{
    res.setHeader("Access-Control-Allow-Origin","*");
    res.setHeader(
      "Access-Control-Allow-Headers",
      "Origin, X-Requested-With, content-Type, Accept");
      res.setHeader(
        "Access-Control-Allow-Methods",
        "GET, POST, PATCH, DELETE, OPTIONS, PUT");
    next()
  })

const saveChartHandle = (req,res,next) => {
    let message = 'Flow Chart Saved Successfully';
        try {
            fs.openSync('./files/Flow_Chart.txt','w')
            const s = req.body.data.str;
            s.forEach(element => {
                fs.appendFileSync('./files/Flow_Chart.txt',element + os.EOL);   
            });
            fs.writeFile("./files/Flow_Chart_Json.json", JSON.stringify(req.body.data.jsonObj), function(err) {
                if(err) {
                    return console.log(err);
                }
            })
          } catch (err) {
              message = 'Fail To Save The Flow Chart'
          }
    res.json({
        message: message,
    });
};

app.post('/SaveChartToFile',saveChartHandle)
app.post('/api/SaveChartToFile',saveChartHandle)


var server = app.listen(3000,()=>{
    var host = server.address().address;
    console.log(host);
    var port = server.address().port;
    console.log('running at http://' + host + ':' + port)
    console.log('Listen...')
})
