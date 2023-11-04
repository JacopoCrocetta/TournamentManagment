import { Inject, Injectable } from "@nestjs/common";
import { IParticipantsRepository } from "src/core/repository/participants.repository";

@Injectable()
export class PartecipantDelete {
  constructor(
    @Inject() private participantsRepository: IParticipantsRepository
  ) {}

  delete = async (id: number): Promise<boolean> => {
    this.participantsRepository.delete(id);

    return this.participantsRepository.getById(id) == null;
  };
}
