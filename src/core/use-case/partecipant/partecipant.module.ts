import { Module } from "@nestjs/common";
import { PartecipantDelete } from "./partecipant-delete";
import { PartecipantCreate } from "./partecipant-create";
import { ParticipantPage } from "./partecipant-page";

const providers = [PartecipantCreate, PartecipantDelete, ParticipantPage];

@Module({ providers, exports: [...providers] })
export class PartecipantModule{}