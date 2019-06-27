const express = require('express');
var app = express()
var bodyParser = require("body-parser");
const fs = require('fs');
var os = require("os");
var config = require('./config.json')
var readEachLineSync = require('read-each-line-sync')
var path = require('path');

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


//Post Request//

// This middleware save data of one flow chart to txt file to "scripts" dir, with agreed 
// API for the SIP server. 
app.post('/api/SaveChartToFile',(req,res,next) => {
    let msg = 'Flow Chart Activated Successfully';
        try {
            fs.openSync(config.txtFilePath,'w')
            const lineVector = req.body.data.str;
            lineVector.forEach(element => {
                fs.appendFileSync(config.txtFilePath,element + os.EOL);   
            });
          } catch (err) {
            msg = 'Fail To Save The Flow Chart'
          }
    res.status(200).json({
        message: msg,
    });
});

// This middleware save data of one flow chart to JSON file to "json" dir
app.post('/api/SaveJsonChart',(req,res,next) => {
    let msg = 'Flow Chart Saved Successfully';
    let flag = false;
    let fileName = req.body.data.fileName + '.json';
    let filePath = path.join(config.jsonFilePath, fileName)
    let i = 1;
    while(fs.existsSync(filePath)){
        flag = true;
        fileName = req.body.data.fileName + + i +'.json'
        filePath = path.join(config.jsonFilePath, fileName)
        i++;
    }
    fs.writeFile(filePath, JSON.stringify(req.body.data.jsonObj), function(err) {
        if(err) {
            msg = 'Fail To Save The Flow Chart'
            res.status(500).json({
                msg:msg
            })
        } else {
            res.json({
                name:fileName.split('.')[0],
                status:200,
                flag:flag
            })
        }
    })
    
});

//Get Request//

//This middleware returns back all the names of the JSON files.
app.get('/api/GetJsonCharts',(req,res,next) => {
    var flowChartNames = [];
    try{
        fs.readdirSync(config.jsonFilePath).forEach(file => {
            flowChartNames.push(file.split('.')[0])
        });
        res.status(200).json({
            jsonName: flowChartNames,
            msg:'OK'
        })
    } catch {
        res.status(500).json({
            msg:'Failed'
        })
    }
})

//This middleware returns back the content of specific JSON file.
app.get('/api/GetJsonFile/:name',(req,res,next) => {
    try{
        var contents = fs.readFileSync(path.join(config.jsonFilePath,
                        req.params.name+'.json'));
        var jsonContent = JSON.parse(contents);
        res.json({
                msg:'OK',
                data:jsonContent
            })  
    } catch {
        res.status(500).json({
            msg:'Failed'
        })
    }
})

//This middleware returns back all the relevant data from the clusters file.
app.get('/api/GetClusters',(req,res,next) => {
    var clusterNames = [];
    var clusterContent = {}
    try {
        fs.readdirSync(config.clustersPath).forEach(file => {
            let fileName = file.split('.')[0]
            clusterNames.push(fileName)
            let cluster = {}
            cluster.name = fileName;
            cluster.questions = {}
            var lineNum = 0;
            readEachLineSync(path.join(config.clustersPath,file),(line) =>{
                let questionDetail = line.split(',');
                cluster.questions[lineNum] = {
                    desc: questionDetail[0],
                    type: questionDetail[1],
                    name: questionDetail[2]
                }
                lineNum++;
            });
            clusterContent[cluster.name] = cluster
        });
        res.json({
            msg:'OK',
            clustersName: clusterNames,
            clustersContent: clusterContent
        });
    } catch(err){
        res.json({
            msg:'Failed'
        });
    }
});

//Delete Request//

//This middleware delete some JSON files, depend on the user request.
app.delete('/api/DeleteJsonFiles',(req,res,next) => {
    req.body.forEach(element => {
        try{
            fs.unlinkSync(path.join(config.jsonFilePath,element+'.json'))
        } catch(err) {
            res.status(500).send({
                msg:'Failed'
            });
        }
    })
    console.log('Json Files Was Deleted!')
    res.json({
        msg:'nice!'
    });
});

var server = app.listen(config.port ,()=>{
    console.log('Listen...')
})
