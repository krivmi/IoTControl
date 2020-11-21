IoT Control

Tento projekt bude zaměřen na příjem a odesílání dat z chipu ESP8266 přes internet do android aplikace v telefonu. 
Aplikace bude umět připojit se k zařízení v mé domacnosti. Konkrétně se bude jednat o ovládání větráku, zobrazování teploty a vlhkosti v místnosti.
Dále bude aplikace umět vykreslovat grafy teploty a vlhkosti v závislosti na čase. Aplikace bude také posílat notifikace, pokud vlhkost v pokoji přesáhne limit. 
Komunikovat bude zkrze webový server a technologii JSON. Běžné uživatelské nastavení se bude ukládat do SharedPreferences. V aplikaci také budou figurovat vlákna, aby stahování dat z webové aplikace neblokovalo hlavní vlákno aplikace. 
