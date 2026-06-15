function camelToKebabCase(str) {
    return str.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
}

function kebabToWhiteSpacedCase(str){
    return str.replace(/([A-Z])([A-Z])/g, `$1 $2`);
}

function whiteSpacedCamelCase(str){
    return str.replace(/([a-z])([A-Z])/g, `$1 $2`);
}

function camelToLabelStyleCase(str){
    return str.replace(/([A-Z])/g, ' $1').replace(/^./, str=> str.toUpperCase());
}

function camelCaseToUpperCase(str){

    return str.replace(/([A-Z])/g, ' $1').toUpperCase();

}