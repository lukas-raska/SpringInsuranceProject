<h1>SpringBoot Insurance Project</h1>

<p>Tento projekt je mou první webovou aplikací tvořenou v <strong>Javě</strong> a <strong>Spring Boot</strong>. Jedná se o cvičnou aplikaci k osvojení základních principů Spring Bootu a  MVC architektury.</p>

<h4>Co moje aplikace umí:</h4>
<ul>
<li>Umožňuje přihlášení pro dvě entity (klient, zaměstnanec) s rozdílnými rolemi a uživatelskými oprávněními.</li>
<li>Přihlášenému uživateli umožňuje vytvoření pojištění</li>
<li>Ke všem zmíněným entitám umoňuje základní CRUD operace.</li>
</ul>

<h4>Použité nástroje:</h4>
<ul>
<li>psáno v <strong>Intellij IDEA</strong> a <strong>VS Code</strong>, k sestavení projektu používán <strong>Maven</strong></li>
<li>kód psán v Javě s použitím frameworku Spring Boot</li>
<li>k validaci vstupních dat použito <strong><a href="https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html">Java Bean Validation</a></strong></li>
<li>k zobrazení dat v HTML použit šablonovací systém <strong><a href="https://www.thymeleaf.org/">Thymeleaf</a></strong> </li>
<li>k řešení bezpečnosti a nastavování uživatelských oprávnění použito <strong><a href="https://spring.io/projects/spring-security">Spring Security</a></strong> a <strong><a href="https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity6"> Thymeleaf extras Spring Security 6</a></strong></li>
  <li>ke stylování použit framework <strong><a href=https://getbootstrap.com/>Bootstrap</a></strong></li>
</ul>

<h4>Jak aplikaci spustit:</h4>
<ol>
<li>stáhnout kód</li>
<li>spustit databázi (aplikace nastavena pro MySQL/MariaDB)
<li>upravit datasource v souboru application.properties, případně driver v pom.xml s ohledem na použitou databázi</li>
<li>spustit přes IDE
<li>případně po <em>mvn clean install</em> přes <em>"target/SpringInsuranceProject-1.0-SNAPSHOT.jar"</em></li>
</ol>
<p>Aplikace je přizpůsobena pouze pro spuštění na lokálním počítači  </p>

<h4>Závěrem:</h4>
Budu rád, pokud si mou aplikaci kdokoli vyzkouší a dá mi zpětnou vazbu.



