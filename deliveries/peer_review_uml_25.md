# Peer-Review 1: UML

Yixin Liu, Domenico Nobile, Claudio Paloscia

Gruppo 26

Valutazione del diagramma UML delle classi del gruppo 25.

## Lati positivi

* Nel model non abbiamo rilevato una classe "centrale", ovvero responsabile della conservazione dei riferimenti a tutte le altre classi e della chiamata dei loro metodi. Vi sono infatti varie classi che si occupano di questo (ad esempio Match, Realm, ...). Ciò garantisce maggiore flessibilità e soprattutto agevola la lettura del codice, a costo, tuttavia, di un Controller più complesso.
* Riteniamo che non rendere studenti, professori, monete e tessere "no entry" delle classi sia una scelta molto appropriata, dal momento che suddette entità non necessiterebbero di una quantità di attributi e metodi tale da giustificare una loro reificazione.
* Corretto uso del pattern Singleton.

## Lati negativi

* La view pare non essere sempre aggiornata per quanto concerne le influenze, dal momento che esse si aggiornano solo al passaggio di madre natura, come indicato nel commento della classe Match, e non a seguito dell’effettiva modifica dello stato dell’isola (ad esempio aggiunta studente).
* L'esistenza della classe CoinManagerObserver ci è sembrata una soluzione un po' macchinosa. Ad esempio l'observer potrebbe essere la SchoolBoard stessa. Non possiamo non osservare, tuttavia, che si tratterebbe in ogni caso di logica applicativa "nascosta" nel model, che in teoria dovrebbe fornire solo delle primitive finalizzate alla lettura e modifica dello stato.
* Molti attributi e metodi della classe Island dovrebbero essere spostati su Archipelago (ad esempio, perché madre natura sulla _singola_ isola?). Ciò mette in dubbio l'esistenza stessa della classe Island, dal momento che l'unica informazione che ha senso di essere legata a un'isola singola (e non al gruppo di isole unite) è la torre.
* Non è chiaro come possa funzionare il metodo putStudents() in CloudTile. Da dove li pesca? Non ha un riferimento a Bag.

## Confronto tra le architetture

L'architettura del gruppo 25 è per molti aspetti simile alla nostra. Riteniamo però opportuno sottolineare due grandi differenze:
1. **Granularità**: in questa architettura sono presenti molte classi che il nostro gruppo ha deciso di accorpare. Questo porta a una maggiore rigidità del nostro modello, in favore di una maggiore semplicità. Il grado di modularità scelto dal gruppo 25 infatti permette una maggiore adattabilità a eventuali modifiche e aggiunte che potrebbero rivelarsi necessarie in futuro, a discapito di una complessità dal punto di vista implementativo.
2. **Centralizzazione del model**: A differenza dell'approccio utilizzato in questo modello, il nostro gruppo ha scelto di implementare il pattern Facade. La classe centrale GameModel si occupa di reindirizzare le chiamate del controller alle classi corrispondenti, portando così a una maggiore semplicità del controller che deve avere solo un riferimento a GameModel.
