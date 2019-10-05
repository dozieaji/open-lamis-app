/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Idris
 */

@RestController
@RequestMapping(value = "/pharmacy")
@Api(tags = "Pharmacy" , description = " ")
public class PharmacyController {
//
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private PharmacyService pharmacyService;
//
//    @Autowired
//    private PharmacyJdbc pharmacyJdbc;
//
//    @Autowired
//    private RegimenJdbc regimenJdbc;
//
//    @InitBinder
//    public void initBinder(WebDataBinder webDataBinder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        dateFormat.setLenient(false);
//        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String getPharmacy(@RequestParam("patientId") String patientId, @RequestParam("pharmacyId") String pharmacyId, HttpSession session) {
//        session.setAttribute("patientId", patientId);
//        session.setAttribute("pharmacyId", pharmacyId);
//        return "pharmacy";
//    }
//
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public String getPharmacysearch() {
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//     public String save(@RequestBody Pharmacy pharmacy,  Model model, HttpSession session) throws ServletException, IOException, ParseException {
//        User user = new User();
//        if(session.getAttribute("user") != null) user = (User) session.getAttribute("user");
//
//
//        pharmacy.setFacilityId(user.getFacilityId());
//        pharmacy.setId(user.getId());
//        pharmacy.setTimeUploaded(new Date());
//        pharmacy.setUploaded(0);
//        pharmacyService.save(pharmacy);
//        //Delete any drug refill done on this date
//        deleteByDateVisit(pharmacy);
//
//
//
//
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public String delete(@RequestBody Pharmacy pharmacy, Model model) throws ServletException, IOException, ParseException {
//        deleteByDateVisit(pharmacy);
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/findByPharmacyId", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findByPharmacyId(@RequestParam("pharmacyId") int pharmacyId) {
//        Pharmacy pharmacy = pharmacyService.findByPharmacyId(pharmacyId);
//        Map<String, Object> map = new HashMap<>();
//        map.put("pharmacyList", pharmacy);
//        return map;
//    }
//
//    @RequestMapping(value = "/findByCachedId", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findByCachedId(HttpSession session) {
//        List<Object> patientList = new ArrayList<>();
//        List<Object> pharmacyList = new ArrayList<>();
//
//        String patientId = "0";
//        if(session.getAttribute("patientId") != null) patientId = (String) session.getAttribute("patientId");
//        if(!patientId.equals("0")) {
//            Patient patient = patientService.findByPatientId(Integer.parseInt(patientId));
//            patientList.add(patient);
//        }
//
//        String pharmacyId = "0";
//        if(session.getAttribute("pharmacyId") != null) pharmacyId = (String) session.getAttribute("pharmacyId");
//        if(!pharmacyId.equals("0")) {
//            Pharmacy pharmacy = pharmacyService.findByPharmacyId(Integer.parseInt(pharmacyId));
//            pharmacyList.add(pharmacy);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("patientList", patientList);
//        map.put("pharmacyList", pharmacyList);
//        return map;
//    }
//
//    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findAll(@RequestParam("patientId") long patientId) {
//        return pharmacyJdbc.findAll(patientId);
//    }
//
//    @RequestMapping(value = "/findRegimen", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findRegimen(@RequestParam("regimenlineIds") String regimenlineIds) {
//        return regimenJdbc.findAll(regimenlineIds);
//    }
//
//    @RequestMapping(value = "/findRegimenByRegimenlineName", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findRegimenByRegimenlineName(@RequestParam String name) {
//        return regimenJdbc.findRegimenByRegimenlineName(name);
//    }
//
//    private void deleteByDateVisit(Pharmacy pharmacy) {
//        List<Pharmacy> list = pharmacyService.findByDateVisit(pharmacy.getPharmacyId(), pharmacy.getDateVisit());
//        for (Pharmacy pharm : list) {
//           pharmacyService.delete(pharm);
//        }
//    }


}
