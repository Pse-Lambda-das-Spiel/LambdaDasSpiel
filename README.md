LambdaDasSpiel
==============

Unser Programm befindet sich in dem Verzeichnis qualtaetssicherung/Projekt/pse_lambda_submission.

Zur erfolgreichen Programmausführung und -einbindung wird benötigt:

- JDK 7+ (ggf. die Compiler Compliance auf 1.7 setzen)
- Android SDK 20 oder höher
- Die Android-Build-Tools-Version 20.0.0 (Eine höhere Build-Tools-Version ist möglich, 
					diese muss aber unter project/pse_lambda_submission/android/build.gradle bei buildToolsVersion entsprechend gesetzt werden)
- Eine für die Android-Entwicklung eingerichtete IDE
- Das Gradle-Plugin für diese IDE auf dem neuesten Stand (bei den Gradle-Plugin Einstellungen sollte "Use Gradle wrapper`s default" gesetzt sein)
- Die entsprechenden üblichen Umgebungsvariablen für Java (JAVA_HOME) und Android(ANDROID_HOME)

Importieren in die IDE:
- Zuerst muss bei pse_lambda_submission/local.properties der Pfad(absoluter Pfad) zur installierten Android-SDK angegeben werden
- Ggf. muss der Wert bei buildToolsVersion in pse_lambda_submission/android/build.gradle angepasst werden (siehe oben)
- Dann lässt sich das Projekt über die IDE importieren:
	- für Eclipse:
		File -> Import -> Gradle -> Gradle Project ausführen, bei "Browse" den Pfad zu pse_lambda_submission/build.gradle angeben,
		dann auf "Build Model" drücken,anschließend das Haupt-Projekt und alle Sub-Projekte auswählen und auf "Finish" drücken
	- für Intellij IDEA:
		Über "Import Project" zu pse_lambda_submission/android/build.gradle navigieren, bestätigen, im nächsten Dialog alle Einstellungen auf den Standardwerten belassen
		und nochmals bestätigen
	- für NetBeans:
		File -> Open Project... ausführen, dann zu pse_lambda_submission/android/build.gradle navigieren und dann "Open Project" drücken


Ggf. muss noch die Compiler Compliance auf Java 1.7 gesetzt werden.
Die Junit-Tests befinden sich unter einem einzelnen separaten Verzeichnis.
Das Desktop-Projekt lässt sich als Java Application starten, das Android-Projekt als Android-Application
