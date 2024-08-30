function setConnected(connected) {
    $("#connect").text("Connected").attr("disabled", true);
    $("#send").attr("disabled", false);
}

// Função para conectar ao WebSocket
function connectWebSocket() {
    const socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({
        Authorization: `Bearer ${localStorage.getItem("Bearer Token")}`
    }, (frame) => {
        console.log(frame);
        setConnected();

        stompClient.subscribe("/topic/messages", (message) => {
            const data = JSON.parse(message.body);
            console.log(data);

            $("#tbody").append(`<tr><td>${data.message}</td><td>${data.from}</td></tr>`);
        });
    }, (error) => {
        console.error('Error during WebSocket connection:', error);
    });
}

// Manipulador de envio do formulário de login
$("#login-form").submit((e) => {
    e.preventDefault();

    // Atualiza os valores do usuário e senha
    var username = document.querySelector('#username').value;
    var password = document.querySelector('#password').value;

    // Cria um objeto com os dados de login
    var data = {
        userName: username,
        password: password
    };

    fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Login failed.');
        }
        return response.json();
    }).then(data => {
        console.log(data);
        localStorage.setItem('Bearer Token', data.token);
        localStorage.setItem("username", username); // Atualiza o armazenamento do username
        $("#login-div").hide();
        $("#connect-div").removeClass("d-none");
        connectWebSocket(); // Chama a função de conexão WebSocket após login
        setConnected();
    }).catch(err => {
        alert("Invalid username or password");
    });
});

// Manipulador de envio do formulário de mensagens
$("#send-form").submit((e) => {
    e.preventDefault();
    stompClient.send("/app/chat", {}, JSON.stringify({
        to: $("#login").val(),  // Nome do usuário destinatário
        message: $("#message").val(),
        from: localStorage.getItem("username")  // Nome do usuário remetente
    }));
});
