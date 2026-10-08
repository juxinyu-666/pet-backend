package com.example.demo.controller;
import com.example.demo.bean.Pet;
import com.example.demo.service.PetService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pet")
@CrossOrigin
public class PetController {

    @Resource
    private PetService petService;

    @GetMapping("/list")
    public List<Pet> getPetList(){
        return petService.findAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<Pet> getPetByOwner(@PathVariable Integer ownerId){
        return petService.findByOwnerId(ownerId);
    }

    @PostMapping("/add")
    public int addPet(@RequestBody Pet pet){
        return petService.add(pet);
    }

    @DeleteMapping("/del/{petId}")
    public int deletePet(@PathVariable Integer petId) {
        return petService.delById(petId);
    }

    @PutMapping("/update")
    public int updatePet(@RequestBody Pet pet){
        return petService.updateById(pet);
    }
}
