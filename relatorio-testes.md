# Relatório de Teste de Software - Caixa Eletrônico

## 1. Informações do Projeto
* **Nome do Projeto**: Caixa Eletrônico (Java / Maven / JUnit 5)
* **Link do Projeto no GitHub**: [https://github.com/VonLanplace/Caixa-Eletronico](https://github.com/VonLanplace/Caixa-Eletronico)
* **Responsável**: Lucas Pereira de Mattos Sartorelli

---

## 2. Explicação do Deploy

Por se tratar de uma aplicação em Java utilizando **Swing** (`JOptionPane`), o projeto não possui deploy web tradicional (nuvem/servidor HTTP). 
* **Modo de Execução**:
```shell
  # 1. Compilação e empacotamento via Maven: `mvn clean package`
  mvn clean package
  # 2. Execução do artefato gerado: `java -jar target/caixa-1.0-SNAPSHOT.jar`
  java -jar target/caixa-1.0-SNAPSHOT.jar
```

---

## 3. Classes Testadas

**`edu.fatec.model.Cedula`**: Responsável pelo controle de quantidade, valores e cálculo de totais por cédula (R$ 200, 100, 50, 20, 10).

**`edu.fatec.model.Banco`**: Agregador de saques por código de banco, responsável por calcular o maior saque, menor saque, média e valor total dos saques.

**`edu.fatec.service.CaixaEletronicoService`**: Núcleo de regras de negócio, gerencia o saldo total do caixa, limite de 100 saques, algoritmo de distribuição de cédulas e validações de exceção.

---

## 4. Testes Unitários Realizados

1. **`CedulaTest`:**
   - `testCriacaoETotal`: Valida a criação de cédulas, o cálculo do valor total e o `toString()` da classe Cedula.
   - `testAdicionar`: Valida a adição de notas em cédulas.
   - `testRetirar`: Valida a retirada de notas e reação a valores negativos.

2. **`BancoTest`:**
   - `testCriacaoBanco`: Valida a inicialização do banco com valores zerados.
   - `testEstatisticas`: Valida os cálculos feitos pelo banco relativamente ao maior e menor saque, média, quantidade de saques feitos em um banco.  

3. **`CaixaEletronicoServiceTest`:**
   - `testInicializacaoPadrao`: Verifica o saldo inicial padrão do caixa, chumbado na Service, e contadores zerados.
   - `testSaqueValido`: Testa múltiplos valores de saque válidos e composição de cédulas esperada.
   - `testSaqueValorInvalido`: Valida o lançamento de erros para valores zero ou negativos.
   - `testSaqueExcedeSaldo`: Valida o lançamento de erros quando o saque excede o saldo disponível.
   - `testCaixaSemNotas`: Valida o lançamento de erro quando o caixa está sem cédulas.
   - `testValorImpossivelCompor`: Valida o lançamento de erros para valores que não podem ser sacados com as cédulas disponíveis por padrão.

---

## 5. Resultado das Compilações e Resultados dos Testes

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running edu.fatec.model.BancoTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.180 s -- in edu.fatec.model.BancoTest
[INFO] Running edu.fatec.model.CedulaTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.054 s -- in edu.fatec.model.CedulaTest
[INFO] Running edu.fatec.AppTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in edu.fatec.AppTest
[INFO] Running edu.fatec.service.CaixaEletronicoServiceTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.072 s -- in edu.fatec.service.CaixaEletronicoServiceTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.501 s
[INFO] Finished at: 2026-10-02T13:04:16-03:00
[INFO] ------------------------------------------------------------------------
```

---

## 6. Conclusão 

O sistema do Caixa eletrônico é robusto tendo várias camadas de validações e tratamento de excessões.
Sendo os maiores problemas encontrados quebras de regras de POO, com o uso de JOptionPanes dentro das camadas controller e service e a falta de injeção de dependências.