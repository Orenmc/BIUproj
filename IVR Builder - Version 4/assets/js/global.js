function initEmptyFlowChart() {
    var data = {
        operators: {
            Start: {
                id: 'Start',
                top: 10,
                left: 10,
                properties: {
                    title: 'Start',
                    inputs: {},
                    outputs: {
                        output_1: {
                            label: '',
                        }
                    }
                }
            }
        }
    }
    $('#FlowChartDropDown').val('Empty')
    $('#ivr').flowchart('setData', data);
}

/**
 * config of local host and server
 */
function clientConfig(){
    return {local: 'http://localhost:3000/api/', server: 'http://82.81.231.86:8888/api/'}
}

/**
 * init all events
 */
function initButtonsEvent(){
    initPlayModalEvent();
    initFunctionalButtonsEvent();
    modalsSaveChanges();
}
/**
 * disable TTS/WAV in play modal.
 */
function initPlayModalEvent(){
    $('#play-wav-name').keyup(function () {
        if ($('#play-wav-name').val() != '') {
            $('#play-tts-name').attr('disabled', 'disabled');
        }
        else {
            $('#play-tts-name').removeAttr('disabled');
        }
    });
    $('#play-tts-name').keyup(function () {
        if ($('#play-tts-name').val() != '') {
            $('#play-wav-name').attr('disabled', 'disabled');
        }
        else {
            $('#play-wav-name').removeAttr('disabled');
        }
    });
}
/**
 * init all functional buttons events (the button in bottom)
 */
function initFunctionalButtonsEvent(){
    // prevent user from entering dots in file name when saved in server
    $('#file-name').keydown(function(e){
        console.log('2')
        if(e.keyCode == 190 || e.keyCode == 110){
            alert('Can Use . For File Name')
            e.preventDefault(); 
        }
    })
    // delete selected operator
    $('#delete_selected_button').click(function () {
        $('#ivr').flowchart('deleteSelected');
    });
    // save the flowchart in server, as json
    $('#server_save_button').click(function () {
        var data = $('#ivr').flowchart('getData');
        //check if there is data in flowchart
        if (Object.keys(data.operators).length == 1) {
            // 1 because of Start Op
            alert('Can Not Save Empty Flow Chart')
            return false
        } else {
            $('#ivr').flowchart('saveFlowchartAsJson', $('#file-name').val());
        }
    });
    // save file for SIP in server (as txt file)
    $('#activate_button').click(function () {
        $('#ivr').flowchart('saveTextForSIP');

    });

    // save data to local storage
    $('#get_data').click(function () {
        var data = $('#ivr').flowchart('getData');
        var JsonData = JSON.stringify(data);
        localStorage.setItem("data", JsonData);

    });
    // get data from localstorage and store it in data
    $('#set_data').click(function () {
        var data = JSON.parse(localStorage.getItem("data"));
        $('#ivr').flowchart('setData', data);
    });
    // delete files from server (json files of older flowcharts)
    $('#delete_button').click(function () {
        var userConfirm = confirm("Are You Sure You Want To Delete These Files?");
        if (userConfirm == false) {
            return false
        }
        var selected = [];
        $('#FlowChartFiles input:checked').each(function () {
            selected.push($(this).val());
        });
        let config = clientConfig();
        /**
         * if you work with local host, else change to config.server
         */
        const jsonUrl = config.local +'DeleteJsonFiles';
        fetch(jsonUrl, {
            method: 'delete',
            body: JSON.stringify(selected),
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(res => {
                if (res.statusText == 'OK') {
                    selected.forEach(element => {
                        if ($('#FlowChartDropDown').val() == element) {
                            initEmptyFlowChart()
                        }
                        $("#FlowChartDropDown option[value=" + element + "]").remove();
                    })
                } else {
                    alert('Can Not Delete Files - Internal Server Error')
                }
            }).catch(function () {
                alert('Failed To Get The Exists Flow Charts - Server Is Off')
            })


    });
    $('#clear').click(function () {
        var txt;
        var r = confirm("Save Your State!\n Are You Sure?");
        if (r == true) {
            initEmptyFlowChart()
        }
        $("#printToHTML").empty();
    });
}

function modalsSaveChanges() {
    $('.save_changes').click(function () {
        //need to use this for popup initilized
        let opId = localStorage.getItem("operatorID")
        let prop = $('#ivr').flowchart('getOperatorProperties', opId);
        let dict = {};
        // change dynamiclly 
        if (prop.class == 'flowchart-play') {
            dict = {
                title: $('#play-title-name').val(),
                wav: $('#play-wav-name').val(),
                tts: $('#play-tts-name').val(),
                time: $('#play-time-name').val()
            }
        } else if (prop.class == 'flowchart-logic') {
            var outputs = []
            var l = $('div#outputDiv :input');
            $('div#outputDiv :input').each(function () {
                var input = $(this);
                let newLabel = input['0'].placeholder;
                if (input.val() !== '') {
                    newLabel = input.val()
                }
                outputs.push(newLabel);
            });
            dict = {
                title: $('#logic-title-name').val(),
                return_type: $('#logic-return-name').val(),
                input_size: $('#logic-inputs-name').val(),
                output_size: $('#logic-outputs-name').val(),
                outputs_labels: outputs
            }

        } else if (prop.class == 'flowchart-route') {
            dict = {
                title: $('#route-title-name').val(),
                route: $('#route-route-name').val(),
            }

        } else if (prop.class == 'flowchart-cluster') {
            if ($('#clusterDropDown').val() == 'Empty') {
                alert('No Cluster Was Choosen')
                return false;
            }
            dict = {
                title: $('#cluster-title-name').val(),
                cluster: $('#clusterDropDown').val()
            }
        } else if (prop.class == 'flowchart-getInput') {
            dict = {
                title: $('#get-title-name').val(),
                variable_name: $('#get-variable-name').val()
            }
        }
        $('#ivr').flowchart('setOperatorChanges', opId, dict)
    })

}

function onOutputChange() {
    //need to use this for popup initilized
    let opId = localStorage.getItem("operatorID")
    let prop = $('#ivr').flowchart('getOperatorProperties', opId);
    $('#Logic').empty()
    console.log('test')
    out_size = $('#logic-outputs-name').val()

    let i = out_size

    var formgroup = $('<div/>', {
        class: 'form-group',
        id: 'outputDiv'
    })
    for (let j = 0; j < i; j++) {
        formgroup.append($('<label>', {
            class: 'col-form-label',
            text: 'label ' + (j + 1) + ':'
        }));
        let placeHolder = 'Output ' + (j + 1)
        if (prop.outputs['output_' + j]) {
            placeHolder = prop.outputs['output_' + j].label
        }
        formgroup.append($('<input>', {
            class: 'form-control',
            type: 'text',
            placeholder: placeHolder,
            id: j.toString(),
        }));
        $('#Logic').append(formgroup)
    }
}

function getJsonFileFromServer() {
    if ($('#FlowChartDropDown').val() == 'Empty') {
        initEmptyFlowChart()
        return false;
    }
    let config = clientConfig();
    /**
    * if you work with local host, else change to config.server
    */
    const url = config.local + 'GetJsonFile/' + $('#FlowChartDropDown').val();
    fetch(url, {
        method: 'get',
    })
        .then(data => { return data.json() })
        .then(res => {
            if (res.msg == 'OK') {
                $('#ivr').flowchart('setData', res.data);
            } else if (res.msg == 'Failed') {
                alert('Failed To Get The Flow Chart - Internal Server Error')
            }
        }).catch(function () {
            alert('Failed To Get The Flow Chart - Server Is Off')
        })
}

function getJsonFileName() {
    let config = clientConfig();
    /**
    * if you work with local host, else change to config.server
    */  
    const jsonUrl = config.local + 'GetJsonCharts';
    fetch(jsonUrl, {
        method: 'get'
    })
        .then(data => { return data.json() })
        .then(res => {
            if (res.msg == 'OK') {
                $("#FlowChartFiles").empty();
                res.jsonName.forEach(element => {
                    //Add the file names to FlowChart DropDown
                    var o = new Option(element, "value");
                    o.value = element;
                    $(o).html(element);
                    $("#FlowChartDropDown").append(o);
                });
            } else if (res.msg == 'Failed') {
                alert('Failed To Get The Exists Flow Charts - Internal Server Error')
            }
        }).catch(function () {
            alert('Failed To Get The Exists Flow Charts - Server Is Off')
        })
}

function initDeleteModal() {
    let config = clientConfig();
    /**
    * if you work with local host, else change to config.server
    */
    const jsonUrl = config.local + 'GetJsonCharts';
    fetch(jsonUrl, {
        method: 'get'
    })
        .then(data => { return data.json() })
        .then(res => {
            $("#FlowChartFiles").empty();
            res.jsonName.forEach(element => {
                formgroup.append($('<input>', {
                    class: 'form-check-input',
                    type: 'checkbox',
                    value: element
                }));
                formgroup.append($('<label>', {
                    class: 'form-check-label',
                    text: element
                }));
                $('#FlowChartFiles').append(formgroup)
            });
        }).catch(function () {
            alert('Failed To Get The Exists Flow Charts - Server Is Off')
        })

    var formgroup = $('<div/>', {
        class: 'form-check',
    })
}