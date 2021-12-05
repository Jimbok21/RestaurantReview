package com.example.courseworkpcversion

/*
 * Data model class to store logos and team names from F1
 */
class MyModel {
    var modelName: String? = null
    private var modelImage: Int = 0
    var modelRating: Int = 0

    /*
     * Return the team name
     */
    fun getNames(): String {
        return modelName.toString()
    }

    /*
     * Set a team name
     */
    fun setNames(name: String) {
        this.modelName = name
    }

    /* Return a team logo
     */
    fun getImages(): Int {
        return modelImage
    }

    /* Set a team logo
     */
    fun setImages(image_drawable: Int) {
        this.modelImage = image_drawable
    }

    fun setRating(rating: Int) {
        this.modelRating = rating
    }

    fun getRating(): Int {
        return modelRating
    }
}