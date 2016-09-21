package fr.softeam.starpointsapp.service;

/**
 * Cette Exception est lancé quand on essaye de supprimer un utilisateur qui dirige une communauté.
 * Il faut d'abord changer le leader de la communauté concernée.
 */
public class LeadersCannotBeDeletedException extends Exception {

}
