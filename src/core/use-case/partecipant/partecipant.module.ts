import { Module } from "@nestjs/common";
import { PartecipantDelete } from "./partecipant-delete";
import { PartecipantCreate } from "./partecipant-create";
import { ParticipantPage } from "./partecipant-page";
import { ParticipantUpdate } from "./partecipant-update";


const providers = [PartecipantCreate, PartecipantDelete, ParticipantPage, ParticipantUpdate];

@Module({ providers, exports: [...providers] })
export class PartecipantModule{}