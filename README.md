# eshop project

Nama: Nezzaluna Azzahra  
NPM: 2406495741  
Kelas: AdvPro - B

## Module 2 - CI/CD & DevOps

### Reflection

1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.  
   Dalam pengerjaan exercise ini, beberapa code quality issues yang saya perbaiki mencakup integrasi code analysis dan code coverage reports. Cara saya resolve issues diantaranya adalah memastikan sinkronisasi antara konfigurasi lokal di build.gradle.kts dengan profil proyek di dashboard SonarCloud, terutama pada bagian sonar.projectKey dan sonar.organization untuk menghindari error analysis. Selain itu, dilakukan perbaikan pada konfigurasi plugin JaCoCo dengan mengaktifkan laporan dalam format XML agar data coverage dapat dibaca secara akurat oleh SonarCloud. Untuk menjaga stabilitas pipeline, task pengujian disesuaikan dengan menggunakan filter excludeTestsMatching("\*FunctionalTest") agar functional yang membutuhkan browser tidak dijalankan di lingkungan server GitHub Actions yang tidak memiliki GUI.

   Selain hal-hal terkait integrasi, beberapa code quality issues yang dilaporkan oleh sonarqube diantaranya aalah mencakup penggunaan field injection dan duplikasi literal string. Cara saya resolve issues tersebut adalah saya mengganti @Autowired pada private field menjadi constructor injection di kelas controller dan service, hal tersebut bertujuan untuk meningkatkan immutability serta memudahkan proses unit test. Selain itu, saya mendefinisikan constant untuk mengatasi duplikasi "redirect:/product/list" untuk memenuhi prinsip Clean Code terkait duplication. Terakhir, saya memperbaiki isu cakupan kode pada metode findAll dan findById dengan menambahkan unit test yang meaningful pada ProductServiceImplTest, sehingga memastikan logika bisnis ter-verify sepenuhnya dan code quality issues report di sonarqube ter-resolve dan code coverage meningkat.

2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!  
   Menurut saya, implementasi CI/CD saya saat ini sudah sepenuhnya memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD) karena seluruh workflow dari testing hingga deploy aplikasi telah ter-automate secara menyeluruh. Dalam segi CI, terpenuhi melalui penggunaan tiga workflow GitHub Actions, yaitu ci.yml untuk menjalankan testing secara otomatis, sonarqube.yml untuk melakukan pengecekan code quality serta pelaporan coverage JaCoCo, dan scorecard.yml untuk security scanning supply-chain setiap kali ada aktivitas push atau pull request. Sedangkan dalam segi CD, diimplementasikan melalui fitur "Auto-deploy on commit" di Koyeb yang secara otomatis mendeteksi perubahan pada branch main, kemudian melakukan build ulang menggunakan Dockerfile, dan meng-update aplikasi di prod env tanpa intervensi manual. Dengan demikian, integrasi antara quality scanning secara otomatis dan sistem deployment langsung ini menjamin bahwa setiap perubahan kode yang teruji dapat segera disuguhkan kepada user secara konsisten dan efisien.

## Module 3 - OO Principles & Software Maintainability

### Reflection

1. Explain what principles you apply to your project!

- Single Responsibility = Pada project ini SRP saya apply dengan cara setiap class memiliki satu tanggung jawab yang jelas dan terfokus. Class model (Product, Car) hanya bertanggung jawab merepresentasikan data entity. Class repository (ProductRepository, CarRepository) hanya bertanggung jawab untuk menangani persistence layer dan operasi CRUD terhadap data. Class service (ProductServiceImpl, CarServiceImpl) berfokus pada logika bisnis. Class controller (ProductController, CarController, HomeController) hanya bertanggung jawab untuk menangani HTTP request/response dan view rendering. Sebagai tambahan saya memindahkan generate UUID pada repository ke service agar repository layer hanya berfokus pada operasi CRUD terhadap data.
- Open/Closed Principles = Pada project ini OCP saya apply dengan cara penggunaan interface pada service dan repository layer. Interface ProductService dan CarService memungkinkan penambahan implementasi baru tanpa mengubah kode yang sudah ada. Repository layer juga menggunakan ProductRepositoryInterface dan CarRepositoryInterface sehingga implementasi persistence dapat diganti tanpa memodifikasi service layer.
- Liskov Substitution = Pada project ini LSP saya apply dengan cara implementasi class dapat menggantikan interface-nya tanpa mengubah perilaku program. ProductServiceImpl dapat menggantikan ProductService, dan CarServiceImpl dapat menggantikan CarService dengan baik. Semua method yang didefinisikan dalam interface diimplementasikan sesuai kontrak yang telah ditentukan.
- Interface Segregation = Pada project ini ISP saya apply dengan cara interface yang dibuat tidak memaksa implementor untuk bergantung pada method yang tidak digunakan. ProductService memiliki 5 method yang semuanya relevan dengan domain Product, begitu juga CarService dengan domain Car. Tidak ada method dalam interface yang tidak digunakan atau tidak harus diimplementasi oleh implementor.
- Dependency Inversion = Pada project ini DIP saya apply dengan cara bergantung pada abstraction melalui interface dan constructoe injection daripada actual implementation. Controller bergantung pada interface Service (ProductService, CarService) bukan pada implementasi concrete. Service bergantung pada interface Repository (ProductRepositoryInterface, CarRepositoryInterface) bukan pada class konkret. Dependency injection melalui constructor memastikan loose coupling antar komponen.

2. Explain the advantages of applying SOLID principles to your project with examples.

- Single Responsibility = Keuntungannya adalah maintainability lebih tinggi karena setiap class punya tanggung jawab tunggal. Contoh pada project ini adalah ProductController hanya menangani alur HTTP, sedangkan ProductServiceImpl menangani business logic. Saat ada perubahan UI flow, perubahan cukup di controller tanpa mengubah service.
- Open/Closed Principles = Keuntungannya adalah fitur baru bisa ditambah (extension) tanpa mengubah (modification) kode yang sudah stabil. Contohnya adalah dengan ProductRepositoryInterface dan CarRepositoryInterface, implementasi repository baru (misalnya database) dapat ditambahkan tanpa mengubah contract pada service.
- Liskov Substitution = Keuntungannya adalah implementasi bisa saling menggantikan secara aman selama mengikuti kontrak interface. Contohnya, ProductServiceImpl dapat dipakai di mana pun ProductService dibutuhkan tanpa mengubah perilaku yang diharapkan controller.
- Interface Segregation = Keuntungannya adalah interface tetap kecil, relevan, dan tidak membebani implementor. Contohnya, ProductService berisi operasi khusus Product saja, sehingga class yang mengimplementasikan tidak dipaksa menulis method yang tidak dibutuhkan domain tersebut.
- Dependency Inversion = Keuntungannya adalah coupling berkurang dan testing jadi lebih mudah. Contohnya, ProductServiceImpl bergantung pada ProductRepositoryInterface, sehingga pada unit test kita bisa mengganti dependency dengan mock repository tanpa mengubah kode produksi.

3. Explain the disadvantages of not applying SOLID principles to your project with examples.

- Single Responsibility = Tanpa SRP, satu class menangani banyak urusan sekaligus dan perubahan kecil jadi berisiko besar. Contohnya, jika ProductController sekaligus mengurus validasi bisnis dan akses data, setiap perubahan aturan bisnis bisa merusak alur HTTP atau persistence.
- Open/Closed Principles = Tanpa OCP, penambahan fitur selalu mengubah kode lama dan meningkatkan peluang regresi. Contohnya, jika repository tidak berbasis interface, migrasi dari in-memory ke database memaksa modifikasi langsung pada service yang sudah berjalan.
- Liskov Substitution = Tanpa LSP, implementasi tidak bisa dipertukarkan dengan aman dan muncul perilaku tak terduga. Contohnya, bila implementasi CarService mengubah kontrak dasar (misalnya operasi findById tidak konsisten), controller yang bergantung pada interface dapat gagal saat runtime.
- Interface Segregation = Tanpa ISP, interface menjadi gemuk dan memaksa class mengimplementasikan method yang tidak relevan. Contohnya, satu interface besar untuk semua entitas membuat implementasi Product harus ikut membawa operasi yang sebenarnya hanya milik domain lain.
- Dependency Inversion = Tanpa DIP, ketergantungan langsung ke class konkret membuat sistem kaku dan sulit diuji. Contohnya, jika ProductServiceImpl langsung mengunci ke ProductRepository concrete class, penggantian dependency di test atau saat refactor menjadi lebih sulit dan mahal.

## Module 4 - Test-Driven Development & Refactoring

### Reflection

1. Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this TDD flow is useful enough for you or not. If not, explain things that you need to do next time you make more tests.  
   Penerapan alur TDD melalui siklus Red-Green-Refactor berguna bagi saya karena memberikan panduan terstruktur dalam setiap tahap pengembangan. Dengan menulis tes terlebih dahulu di tahap RED, saya perlu benar-benar memahami kebutuhan fitur dan skenario kegagalan sebelum menulis kode implementasi. Tahap GREEN memberikan kepastian bahwa kode minimal yang saya tulis sudah cukup untuk memenuhi fungsionalitas yang diminta. Terakhir, tahap REFACTOR membantu saya dalam merapikan struktur kode dan menghapus duplikasi tanpa rasa takut merusak sistem yang sudah berjalan. Dengan demikian, TDD memberikan benchmark yang membuat saya merasa lebih percaya diri saat melakukan perubahan atau penambahan fitur baru pada proyek ini.

2. You have created unit tests in Tutorial. Now reflect whether your tests have successfully followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you create more tests.  
   Unit test yang saya buat dalam tutorial ini telah berupaya keras untuk mengikuti prinsip F.I.R.S.T demi menjaga kualitas pengujian yang baik. Prinsip Fast dan Independent berhasil saya terapkan dengan menggunakan mocking (Mockito), sehingga setiap tes berjalan dengan cepat dan tidak memiliki ketergantungan antar satu sama lain. Tes yang saya buat juga bersifat Repeatable dan Self-Validating, yang artinya memberikan hasil yang sama di lingkungan manapun dan otomatis memberikan status lolos atau gagal. Namun, saya menyadari bahwa aspek Timely masih menjadi tantangan terbesar karena terkadang saya merasa perlu untuk mulai menulis kode implementasi terlebih dahulu sebelum tesnya benar-benar matang. Ke depannya, saya ingin lebih disiplin dalam menulis tes secara tepat waktu agar seluruh manfaat dari desain berbasis tes ini bisa saya rasakan sepenuhnya.
