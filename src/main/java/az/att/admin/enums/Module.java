package az.att.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Module {

    VIEW_DASHBOARD("İdarəetmə paneli"),
    VIEW_PRODUCTS("Məhsullar"),
    VIEW_DST("DST"),
    VIEW_DEMANDS("Tələbatlar"),
    VIEW_SUPPLIERS("Satıcılar"),
    VIEW_OFFER_PLACEMENT("Təklifin yerləşdirilməsi"),
    VIEW_MONITORING("Monitorinq"),
    VIEW_CONTRACTS("Müqavilələr"),
    VIEW_TRANSACTIONS("Əməliyyatlar"),
    VIEW_FINANCE("Maliyyə"),
    VIEW_ARCHIVE("Arxiv"),
    VIEW_REPORTS("Hesabatlar"),
    VIEW_QA("Sual-cavab"),
    VIEW_COMPLAINTS("Şikayətlər"),
    VIEW_SETTINGS("Tənzimləmələr");

    private final String label;
}
