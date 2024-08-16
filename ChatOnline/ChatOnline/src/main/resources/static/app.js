// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';

import SockJS from 'SockJS';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;

export function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

export function connect() {
    const socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    }, function (error) {
        console.error('STOMP connection error: ', error);
    });
}

export function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

export function sendMessage() {
    const from = document.getElementById('from').value;
    const text = document.getElementById('text').value;

    if (stompClient) {
        stompClient.send("/app/chat", {}, JSON.stringify({ 'from': from, 'text': text }));
    } else {
        console.error('STOMP client is not connected.');
    }
}

function showMessageOutput(messageOutput) {
    const response = document.getElementById('response');
    const p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(messageOutput.from + ": " + messageOutput.text + " (" + messageOutput.time + ")"));
    response.appendChild(p);
}
