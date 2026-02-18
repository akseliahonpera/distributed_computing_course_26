Kuinka patients taulun id sarake generoidaan. Luodaanko se serverin toimesta vai luetaanko se clientin lähettämästä viestistä?
Tämän hetkisessä toteutuksessa serveri lukee id:n clientin lähettämästä viestistä, jolloin client päättää id:n, mikäli client jättää ko. kentän null:ksi 
ei serveri luo potilasta ollenkaan patients tauluun.

model/RecordTable ja model/PatientTable pitää katsoa kuntoon. Ne on tilapäisiä kikkareita guin tunkkausta varten. 

Patientin/recordin hakumetodi hakulomakkeen avulla pitää toteuttaa service- ja APIluokkiin. Kts. PatientSearchFrame ja RecordSearchFrame:
```
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.addActionListener(e -> {
            patientDataPanel1.updatePatientData(patient);
            // TODO: need to implement search method in patient service/API
            // can call getAllPatients as a placeholder
            dispose();
        });
```