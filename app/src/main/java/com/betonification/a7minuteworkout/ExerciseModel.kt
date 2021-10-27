package com.betonification.a7minuteworkout

class ExerciseModel(private var id: Int,
                    private var name: String,
                    private var image: Int,
                    private var isCompleted: Boolean,
                    private var isOngoing: Boolean,
                    private var isSelected: Boolean) {
    fun getId(): Int{
        return id
    }
    fun setId(newId: Int){
        this.id = newId
    }
    fun getName(): String{
        return name
    }
    fun setName(newName: String){
        this.name = newName
    }
    fun getImage(): Int{
        return image
    }
    fun setImage(newImage: Int){
        this.image = newImage
    }
    fun getIsCompleted(): Boolean{
        return isCompleted
    }
    fun setIsCompleted(newIsCompleted: Boolean){
        this.isCompleted = newIsCompleted
    }
    fun getIsOngoing(): Boolean{
        return isOngoing
    }
    fun setIsOngoing(newIsOngoing: Boolean){
        this.isOngoing = newIsOngoing
    }
    fun getIsSelected(): Boolean{
        return isSelected
    }
    fun setIsSelected(newIsSelected: Boolean){
        this.isSelected = newIsSelected
    }
}