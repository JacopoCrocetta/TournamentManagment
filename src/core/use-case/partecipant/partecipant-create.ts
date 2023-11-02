import { Inject, Injectable } from "@nestjs/common";
import { IParticipantsRepository } from "src/core/repository/participants.repository";

interface CreateParticipantsInput {}

@Injectable()
export class PartecipantCreate {
  constructor(@Inject() participantsRepository: IParticipantsRepository) {}

  create = async (input: CreateParticipantsInput) => {
    return null;
  };
}
