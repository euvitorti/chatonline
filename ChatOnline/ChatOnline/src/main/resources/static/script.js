
// Função para realizar o login
function login() {
    // Previne o comportamento padrão do formulário
    event.preventDefault();

    // Captura os valores dos campos de entrada
    var username = document.querySelector('#username').value;
    var password = document.querySelector('#password').value;

    // Exibe os valores capturados no console (para fins de verificação)
    console.log("Username:", username);
    console.log("Password:", password);

    // Cria um objeto com os dados de login
    var data = {
        userName: username,
        password: password
    };

    // Envia uma requisição POST para '/login' com os dados em formato JSON
    fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Login failed.');
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('Bearer Token', data.token); // Armazena o token JWT localmente
            window.location.href = 'chat.html'; // Redireciona após o login bem-sucedido

            window.alert("Graças a Deus!");
        })
        .catch(error => {
            document.getElementById('error-message').innerText = 'Login failed. Please check your credentials.';
            console.error('Login error:', error);
        });
}

// Adiciona um event listener para o evento 'submit' do formulário de login
document.querySelector('#login-form').addEventListener('submit', login);
