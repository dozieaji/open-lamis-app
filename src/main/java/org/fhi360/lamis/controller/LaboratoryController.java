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
 * @author user10
 */

@RestController
@RequestMapping(value = "/laboratory")
@Api(tags = "Laboratory" , description = " ")
public class LaboratoryController {
//
//    @Autowired
//    private PatientDao patientService;
//
//    @Autowired
//    private LaboratoryDao laboratoryService;
//
//
//
//    @InitBinder
//    public void initBinder(WebDataBinder webDataBinder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        dateFormat.setLenient(false);
//        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String getLaboratory(@RequestParam("patientId") String patientId, @RequestParam("laboratoryId") String laboratoryId, HttpSession session) {
//        session.setAttribute("patientId", patientId);
//        session.setAttribute("laboratoryId", laboratoryId);
//        return "laboratory";
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String save(@RequestBody Laboratory laboratory, HttpSession session) {
//        User user = new User();
//        if (session.getAttribute("user") != null) {
//            user = (User) session.getAttribute("user");
//        }
//
//        laboratory.setFacility(user.getFacility());
//        laboratory.setUser(user);
//
//        //Delete any laboratory done on this date
//        deleteByDateVisit(laboratory);
//
//        return "laboratorysearch";
//    }
//
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public String delete(@RequestBody Laboratory laboratory, Model model) throws ServletException, IOException, ParseException {
//        deleteByDateVisit(laboratory);
//        //laboratoryService.delete(laboratory);
//        return "laboratorysearch";
//    }
//
//    @RequestMapping(value = "/findByLaboratoryId", method = RequestMethod.GET)
//    public @ResponseBody
//    Map<String, ? extends Object> findByLaboratoryId(@RequestParam("laboratoryId") long laboratoryId) {
//        Laboratory laboratory = laboratoryService.getOne(laboratoryId);
//        Map<String, Object> map = new HashMap<>();
//        map.put("laboratoryList", laboratory);
//        return map;
//    }
//
//    @RequestMapping(value = "/findByCachedId", method = RequestMethod.GET)
//    public @ResponseBody
//    Map<String, ? extends Object> findByCachedId(HttpSession session) {
//        List<Object> patientList = new ArrayList<>();
//        List<Object> laboratoryList = new ArrayList<>();
//
//        String patientId = "0";
//        if (session.getAttribute("patientId") != null) {
//            patientId = (String) session.getAttribute("patientId");
//        }
//        if (!patientId.equals("0")) {
//            Patient patient = patientService.getOne(Long.parseLong(patientId));
//            patientList.add(patient);
//        }
//
//        String laboratoryId = "0";
//        if (session.getAttribute("laboratoryId") != null) {
//            laboratoryId = (String) session.getAttribute("laboratoryId");
//        }
//        if (!laboratoryId.equals("0")) {
//            Laboratory laboratory = laboratoryService.getOne(Long.parseLong(laboratoryId));
//            laboratoryList.add(laboratory);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("patientList", patientList);
//        map.put("laboratoryList", laboratoryList);
//        return map;
//    }
//
//    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
//    public List<Laboratory> findAll(@RequestParam("patientId") long patientId) {
//        return laboratoryService.findByPatient(patientId);
//    }
//
//    @RequestMapping(value = "/findLabtest", method = RequestMethod.GET)
//    public @ResponseBody
//    Map<String, ? extends Object> findLabtest(@RequestParam("labtestgroupIds") String labtestgroupIds) {
//        return labtestJdbc.findAll(labtestgroupIds);
//    }
//
//    private void deleteByDateVisit(Laboratory laboratory) {
//        List<Laboratory> list = laboratoryService.findByDateVisit(laboratory.getPatient().getPatientId(), laboratory.getDateReported());
//        for (Laboratory lab : list) {
//            laboratoryService.delete(lab);
//        }
//    }
}
