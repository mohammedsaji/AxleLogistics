const StringUtils = {
    camelToKebabCase: (str) => {
        return str.replace(/([a-z0-9])([A-Z])/g, '$1-$2').toLowerCase();
    },
    camelToLabelCase: (str) => {
        // camelToLabelStyleCase equivalent
        return str
            .replace(/([A-Z])/g, ' $1')
            .replace(/^./, (match) => match.toUpperCase());
    },
    camelToUpperCase: (str) => {
        // camelCaseToUpperCase equivalent
        return str.replace(/([A-Z])/g, ' $1').toUpperCase();
    },
    kebabToWhiteSpacedCase: (str) => {
        return str.replace(/-/g, ' ');
    },
    whiteSpacedCamelCase: (str) => {
        return str.replace(/([a-z])([A-Z])/g, '$1 $2');
    }
};