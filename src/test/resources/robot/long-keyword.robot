*** Test Cases ***
001 - Execute
    Annuler un ordre permanent de <5.000,00> : créditeur <John> N°compte <5.000,00> : créditeur N°compte <LU85 0000 1111 2222 4444> - débiteur <Jane> N°compte <LU85 6666 7777 8888 9999>

*** Keywords ***
Annuler un ordre permanent de <${Montant_virement}> : créditeur <${Nom_créditeur}> N°compte <${Numero_compte_créditeur}> - débiteur <${Nom_débiteur}> N°compte <${Numero_compte_débiteur}>
    Log    ${Montant_virement}
    Log    ${Nom_créditeur}
    Log    ${Numero_compte_créditeur}
    Log    ${Nom_débiteur}
    Log    ${Numero_compte_débiteur}