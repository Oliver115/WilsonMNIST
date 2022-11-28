'''
 @author Oliver Wilson & Mike Volchko
 @date 26 Nov 2022

 The purpose of this python script is to convert the .npz data files to .csv files
 After they are converted once we can load the .csv files in to our java project

'''

import numpy as np

#Load the .npz data files in
breastData = np.load("C:/Users/Micha/OneDrive/Desktop/FALL22/Artificial Intelligence/VolchkoWilsonMedMNIST/"
                     "data/breastmnist.npz")
chestData = np.load("C:/Users/Micha/OneDrive/Desktop/FALL22/Artificial Intelligence/VolchkoWilsonMedMNIST/"
                    "data/chestmnist.npz")
pneumoniaData = np.load("C:/Users/Micha/OneDrive/Desktop/FALL22/Artificial Intelligence/VolchkoWilsonMedMNIST/"
                        "data/pneumoniamnist.npz")

'''
dataset: breastData, retinaData, pneumoniaData (Actual dataset variables)
type: train, test, val
datasetname: breast, pneumonia, retina
'''
def saveImagesAndLabels(dataset, type, datasetname):
    #Initialize function variables
    images = []
    labels = []
    imageArray = dataset[type + "_images"]
    labelArray = dataset[type + "_labels"]
    imageSavePath = "C:/Users/Micha/OneDrive/Desktop/FALL22/Artificial Intelligence/VolchkoWilsonMedMNIST/data/" \
                    + datasetname + "MNIST/" + datasetname + "_" + type + "_images.csv"
    labelSavePath = "C:/Users/Micha/OneDrive/Desktop/FALL22/Artificial Intelligence/VolchkoWilsonMedMNIST/data/" \
                    + datasetname + "MNIST/" + datasetname + "_" + type + "_labels.csv"
    #tempImage is used to append all the rows together for the image to downsize from a 3D array to a 2D
    tempImage = []

    #Format the images into a 2D array instead of a 3D array
    for image in range (0, len(imageArray)):
        for row in range (0, len(imageArray[0])):
            for column in range (0, len(imageArray[0][0])):
                tempImage.append(imageArray[image][row][column])
        #After an image is completed, append to the images array and wipe the tempImage array
        #Also make sure to append the label to the labels array
        images.append(tempImage)
        tempImage = []
        labels.append(labelArray[image])

    #Convert to NumPy array to write to a csv
    images = np.asarray(images)
    labels = np.asarray(labels)

    np.savetxt(imageSavePath, images, delimiter=",")
    np.savetxt(labelSavePath, labels, delimiter=",")
    print(datasetname + " has been saved in a .csv (" + type + ")")

saveImagesAndLabels(breastData, "train", "breast")
saveImagesAndLabels(pneumoniaData, "train", "pneumonia")
saveImagesAndLabels(chestData, "train", "chest")
saveImagesAndLabels(breastData, "test", "breast")
saveImagesAndLabels(pneumoniaData, "test", "pneumonia")
saveImagesAndLabels(chestData, "test", "chest")
