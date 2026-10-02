# Caixa Eletrônico

Projeto desenvolvido em Java utilizando Maven, com o objetivo de simular as operações básicas de um caixa eletrônico.

O sistema permite carregar notas, realizar saques, controlar a quantidade de cédulas disponíveis e apresentar
estatísticas dos saques separados por banco.

## Tecnologias utilizadas

- Java
- Maven
- JUnit

## Estrutura do projeto

```
.
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── edu
    │           └── fatec
    │               ├── App.java
    │               ├── controller
    │               │   └── CaixaEletronicoController.java
    │               ├── model
    │               │   ├── Banco.java
    │               │   ├── Cedula.java
    │               │   └── Saque.java
    │               └── service
    │                   └── CaixaEletronicoService.java
    └── test
        └── java
            └── edu
                └── fatec
                    ├── AppTest.java
                    ├── model
                    │   ├── BancoTest.java
                    │   └── CedulaTest.java
                    └── service
                        └── CaixaEletronicoServiceTest.java
```

## Funcionalidades

O sistema possui interface gráfica baseada em Swing (`JOptionPane`) e o seguinte menu principal:

```
==============================
       CAIXA ELETRÔNICO
==============================
1 - Carregar Notas
2 - Retirar Notas
3 - Estatística
4 - Mostrar Notas
9 - Fim
==============================
```

### 1 - Carregar Notas

Permite carregar a quantidade de notas disponíveis no caixa.

O sistema trabalha com as seguintes cédulas:

- R$ 10
- R$ 20
- R$ 50
- R$ 100
- R$ 200

Cada tipo de cédula possui inicialmente **6 ocorrências**.

O usuário pode adicionar novas notas ao caixa através da opção **Carregar Notas**.

### 2 - Retirar Notas

Permite que o cliente informe:

- Valor desejado para o saque;
- Código do banco em que possui conta.

O sistema calcula automaticamente as notas que serão entregues, utilizando o critério de **maior para menor valor**.

Por exemplo, para um saque de R$ 280:

```
1 x R$ 200
1 x R$ 50
1 x R$ 20
1 x R$ 10
```

Total:

```
R$ 280
```

As notas utilizadas são retiradas do estoque do caixa.

#### Regras para o saque

O sistema segue as seguintes regras:

1. As notas são utilizadas da maior para a menor.
2. O saque deve ser realizado utilizando as notas disponíveis.
3. O sistema não realiza saque parcial.
4. Caso o valor solicitado seja maior que o saldo disponível no caixa, é exibida a mensagem:
    - `EXCEDEU O LIMITE DO CAIXA`
5. Caso não seja possível montar o valor solicitado com as cédulas disponíveis, o saque não é realizado.
6. O sistema permite no máximo **100 retiradas**.
7. As retiradas também são interrompidas quando não houver mais notas disponíveis no caixa.

### 3 - Estatística

A opção de estatística apresenta os dados dos saques realizados, separados pelo código do banco.

Para cada banco são apresentados:

- Maior valor sacado;
- Menor valor sacado;
- Média dos saques;
- Valor total dos saques.

Também é apresentado o valor restante no caixa.

**Exemplo:**

```
==================================
ESTATÍSTICA
==================================

Banco: 341
Maior saque: R$ 500
Menor saque: R$ 100
Média dos saques: R$ 300.00
Valor total dos saques: R$ 900

Banco: 237
Maior saque: R$ 200
Menor saque: R$ 50
Média dos saques: R$ 125.00
Valor total dos saques: R$ 250

Valor das sobras do caixa: R$ 2850
Quantidade de saques realizados: 5
==================================
```

## Organização das classes

### `App.java`

Classe responsável por iniciar a aplicação e apresentar o menu principal.

`edu.fatec.App`

É o ponto de entrada do programa através do método `public static void main(String[] args)`

### `model/Cedula.java`

Representa uma cédula disponível no caixa.

**Responsabilidades:**

- Armazenar o valor da cédula;
- Controlar a quantidade disponível;
- Adicionar notas;
- Retirar notas;
- Calcular o valor total das notas disponíveis.

**Exemplo:**

```
Valor: R$ 50
Quantidade: 6
Total: R$ 300
```

### `model/Saque.java`

Representa uma operação de saque.

**Armazena informações como:**

- Valor do saque;
- Código do banco;
- Cédulas utilizadas na operação.

### `model/Banco.java`

Representa um banco e mantém os saques realizados por seus clientes.

**A classe é utilizada para calcular:**

- Maior saque;
- Menor saque;
- Média dos saques;
- Total sacado.

### `controller/CaixaEletronicoController.java`

Camada de controle responsável pela interação com o usuário através de caixas de diálogo Swing (`JOptionPane`), validação de entradas e exibição de mensagens.

### `service/CaixaEletronicoService.java`

Contém a principal lógica de negócio do sistema.

**É responsável por:**

- Controlar as cédulas disponíveis;
- Carregar notas;
- Validar saques;
- Calcular as notas que serão entregues;
- Atualizar o estoque de cédulas;
- Registrar os saques;
- Controlar o limite de 100 saques;
- Gerar as estatísticas;
- Calcular o saldo restante do caixa.

## Fluxo do sistema

```
          ┌───────────────────┐
          │      App.java     │
          └─────────┬─────────┘
                    │
                    ▼
          ┌───────────────────┐
          │   Menu Principal  │
          └─────────┬─────────┘
                    │
      ┌─────────────┼────────────────┐
      │             │                │
      ▼             ▼                ▼
Carregar Notas    Retirar       Estatística
      │            Notas             │
      ▼             │                ▼
┌───────────┐       │          ┌───────────┐
│  Cedula   │◄──────┘          │   Banco   │
└───────────┘                  └─────┬─────┘
                                     │
                                     ▼
                                ┌─────────┐
                                │  Saque  │
                                └─────────┘
```

## Como executar

### Pré-requisitos

**É necessário ter instalado:**

- **JDK 17** ou superior;
- **Maven.**

Para verificar a instalação do Java:

`java -version`

Para verificar o Maven:

`mvn -version`

### Compilar o projeto

Na raiz do projeto, execute:

`./mvn clean compile`

### Executar os testes

`mvn test`

### Executar a aplicação

Caso o projeto esteja configurado com o plugin adequado no pom.xml, pode ser utilizado:

`mvn exec:java -Dexec.mainClass="edu.fatec.App"`

Também é possível executar diretamente a classe App.java pela IDE.

### Exemplo de utilização

Inicialmente, o usuário pode selecionar:

```
1 - Carregar Notas
```

E informar a quantidade de notas:

```
Quantidade de notas de R$ 200: 6
Quantidade de notas de R$ 100: 6
Quantidade de notas de R$ 50: 6
Quantidade de notas de R$ 20: 6
Quantidade de notas de R$ 10: 6
```

Depois, selecionando:

```
2 - Retirar Notas
```

O sistema solicita:

```
Digite o valor do saque: R$ 280
Digite o código do banco: 341
```

O caixa poderá entregar:

```
1 x R$ 200
1 x R$ 50
1 x R$ 20
1 x R$ 10
```

Após a operação, as notas entregues são descontadas do estoque.

## Tratamento de limite

Caso o cliente solicite um valor superior ao saldo total disponível no caixa:

```
Digite o valor do saque: R$ 10000

EXCEDEU O LIMITE DO CAIXA
```

O saque não é realizado e as notas permanecem disponíveis.

## Limite de operações

O sistema permite realizar no máximo:

```
100 saques
```

Quando esse limite é atingido, novas retiradas não são permitidas.

Além disso, caso todas as notas sejam retiradas, o sistema informa que não existem mais notas disponíveis.

## Testes

Os testes automatizados estão localizados em:

`src/test/java/edu/fatec/AppTest.java`

Os testes podem ser executados com:

`mvn test`
