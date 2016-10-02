package fr.softeam.starpointsapp.service.exception;

/**
 * Cette exception est lancé lorsque l'on essaye de supprimer une activité avec des contributions rattachées à cette dernière.
 * Il faut d'abord supprimer les contributions avant de supprimer l'activité.
 */
public class ActivityReferencedByContributionsException extends Exception {

}
