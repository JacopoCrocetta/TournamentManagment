import { Inject, Injectable } from "@nestjs/common";
import { Participants } from "src/core/entity/abstract-participants";
import { IParticipantsRepository } from "src/core/repository/participants.repository";

@Injectable()
export class ParticipantPage {
  constructor(
    @Inject() private participantsRepository: IParticipantsRepository
  ) {}

  getById = async (id: number): Promise<Participants> => {
    return this.participantsRepository.getById(id);
  };

  getAll = async (idTournament: number): Promise<Participants[]> => {
    return this.participantsRepository.getAllByIdTournament(idTournament);
  };
}
