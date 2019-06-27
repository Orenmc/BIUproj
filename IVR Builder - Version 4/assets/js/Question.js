class Question {
    constructor(description ,variableType ,variableName){
        this.description = description;
        this.variableName = variableName;
        this.variableType = variableType
    }

    getString(){
        console.log('Question: ' + this.description + '\n'
        + 'type: ' + this.variableType + '\n' + 
        'name: ' + this.variableName)
    }
}