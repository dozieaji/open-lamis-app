var storage = window.localStorage;

var oldFetch = window.fetch;

window.fetch1 = function (input, init = {}) {
    console.log('Options', init.headers);
    var authorization = this.authorization();
    if (!!authorization) {
        init.headers = {
            Authorization: 'Bearer ' + authorization
        };
    }
    console.log('Params', input, init);
    return oldFetch(input, init).then(function (value) {
        if (value.status === 401 || (value.status === 500 &&
            value.detail === 'Full authentication is required to access this resource')) {
            window.location = '/';
        }
        return value;
    });
};

function authorization() {
    var token = storage.getItem('access_token');
    console.log('Token', token);
    if (!!token) {
        window.location = '/';
    }
    return token;
}

function saveAuthorization(token) {
    storage.setItem('access_token', token);
}

function clearAuthorization() {
    storage.clear();
}