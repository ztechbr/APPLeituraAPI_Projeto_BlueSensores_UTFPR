# Monitor de Sensores

Este aplicativo Android foi desenvolvido para realizar a coleta e visualização de dados provenientes de sensores agrícolas via API REST. O sistema permite filtrar leituras por período, código de plantação e parâmetros de paginação, apresentando os resultados de forma analítica e detalhada.

## Arquitetura do Projeto

O projeto segue o padrão de arquitetura **MVVM (Model-View-ViewModel)**, visando a separação de responsabilidades e a facilidade de manutenção:

*   **View**: Composta pelas Activities (`MainActivity` e `ResultsActivity`) e layouts XML. Utiliza **ViewBinding** para acesso seguro às views e **ListAdapter** com **DiffUtil** para otimização de performance nas listas.
*   **ViewModel**: A classe `SensorViewModel` gerencia o estado da UI e encapsula a lógica de negócio. Ela se comunica com o Repository e expõe os dados através de **LiveData**, garantindo que a interface seja atualizada automaticamente em resposta a mudanças nos dados ou estados de carregamento.
*   **Model**: Define a estrutura de dados (`SensorData`) e as respostas da API, utilizando anotações do GSON para o mapeamento JSON.

## Padrão de Acesso a Dados

O acesso aos dados é estruturado em camadas para abstrair a origem das informações:

1.  **Repository Pattern**: A classe `SensorRepository` atua como uma fachada única para o acesso aos dados. Atualmente, ela faz a mediação com a fonte remota, mas sua estrutura permite a implementação futura de cache local (Room) sem impactar as outras camadas.
2.  **Network Layer**: Utiliza a biblioteca **Retrofit** configurada com um `OkHttpClient` que inclui logs de interceptação para depuração. 
3.  **Gerenciamento de Sessão**: O `RetrofitClient` permite a configuração dinâmica do Token de Autorização, que é injetado nos headers de todas as requisições de forma centralizada.

## Visualização de Dados e Performance

*   **Série Temporal**: Foi implementado um componente customizado (`TimeSeriesChartView`) que desenha diretamente no `Canvas`. Ele renderiza um gráfico de linha com preenchimento, processando os dados cronologicamente para mostrar a evolução da temperatura.
*   **Otimização de UI**: Para evitar travamentos em grandes volumes de dados, o gráfico é injetado como um `Header` dentro do `RecyclerView`. Isso mantém a rolagem fluida e evita que o sistema precise renderizar centenas de elementos fora da área visível da tela.
*   **Navegação**: O fluxo de entrada de parâmetros foi separado da tela de resultados para garantir uma interface mais limpa e focada na experiência do usuário.

## Tecnologias Utilizadas

*   **Kotlin** como linguagem principal.
*   **Coroutines** para operações assíncronas e chamadas de rede não bloqueantes.
*   **Jetpack Lifecycle** (ViewModel e LiveData).
*   **Retrofit & GSON** para consumo de APIs REST.
*   **Material Design** para componentes de interface e feedback visual.
