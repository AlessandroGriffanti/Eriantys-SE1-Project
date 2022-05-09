# Peer-Review 2: Protocollo di Comunicazione

Yixin Liu, Domenico Nobile, Claudio Paloscia

Gruppo 26

Valutazione della documentazione del protocollo di comunicazione del gruppo 25.

## Lati positivi

Il protocollo ci sembra particolarmente flessibile ed essenziale. La sintassi UML è stata correttamente utilizzata.

## Lati negativi

Ci preoccupa la centralità che ha il client nel protocollo: nella fase "Joining game" è il client ad effettuare modifiche al model senza che esse siano validate dal server, ad esempio quando vengono pescati gli studenti. Ciò ha grosse ripercussioni sulla sicurezza (un client malevolo potrebbe pescare gli studenti in maniera non casuale). Inoltre dal protocollo non si evince come i client aggiornino il proprio model locale a seguito di un'azione di altri player. Ciò può causare inconsistenze tra i model dei client.

## Confronto

A parte le perplessità appena descritte, il protocollo ci sembra tutto sommato valido. Volevamo, in ultimo, confrontare la politica di utilizzo degli ack con la nostra. Nel nostro protocollo ogni client ha una propria copia del model. Per preservarne la consistenza, ogni modifica proposta dal client dovrà ricevere una conferma da parte del server, solo a seguito della quale il client potrà effettivamente aggiornare il proprio model locale. Ciascun ack (che dunque viene inviato solo dal server al client) viene inviato <u>in broadcast</u>, così da permettere ad ogni client di aggiornare il proprio model. A tal scopo, il "nostro" ack non è privo di attributi, bensì contiene tutte le informazioni per aggiornare i model locali. Si tratta dunque di ack a livello applicativo anziché a livello di trasporto, dal momento che l'integrità dei dati inviati è già garantita dal protocollo TCP.
