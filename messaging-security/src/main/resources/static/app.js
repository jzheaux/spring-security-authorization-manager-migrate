var stompClient = null;
var csrf = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#greetings").html("").show();
    }
    else {
        $("#greetings").hide();
    }
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    var headers = {};
    headers[csrf.headerName] = csrf.token;
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/items', function (items) {
            showItems(JSON.parse(items.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendItem() {
    stompClient.send("/app/write", {}, $("#name").val());
}

function showItems(items) {
    $("#greetings").html("");
    items.forEach(function(item) {
        $("#greetings").append("<li>" + item + "</li>");
    });
}

$(function () {
    $.get("/csrf", (_csrf) => { csrf = _csrf; });
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendItem(); });
});