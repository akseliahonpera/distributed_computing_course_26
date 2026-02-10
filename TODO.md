Kuinka patients taulun id sarake generoidaan. Luodaanko se serverin toimesta vai luetaanko se clientin lähettämästä viestistä?
Tämän hetkisessä toteutuksessa serveri lukee id:n clientin lähettämästä viestistä, jolloin client päättää id:n, mikäli client jättää ko. kentän null:ksi 
ei serveri luo potilasta ollenkaan patients tauluun.
